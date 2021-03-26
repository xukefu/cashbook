package com.xkf.cashbook.controller;

import com.google.common.collect.Lists;
import com.xkf.cashbook.common.constant.ValidateCodeType;
import com.xkf.cashbook.common.result.Result;
import com.xkf.cashbook.common.result.ResultGenerator;
import com.xkf.cashbook.common.utils.AesEncryptUtils;
import com.xkf.cashbook.common.utils.RndUtils;
import com.xkf.cashbook.common.utils.SmsUtils;
import com.xkf.cashbook.jwt.JwtTokenUtil;
import com.xkf.cashbook.pojo.dto.UserDTO;
import com.xkf.cashbook.pojo.vo.LoginVO;
import com.xkf.cashbook.pojo.vo.ValidateCodeVO;
import com.xkf.cashbook.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.xkf.cashbook.common.constant.Constants.*;

@Controller
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Resource
    private UserService userService;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Resource
    private SmsUtils smsUtils;

    @Value("${jwt.aes-seed}")
    private String aesSeed;

    @GetMapping("/login")
    public String login() {
        return "user/login";
    }

    @PostMapping("/sendValidateCode")
    @ResponseBody
    public Result sendValidateCode(@RequestBody ValidateCodeVO validateCodeVO) throws Exception {
        Integer type = validateCodeVO.getType();
        String phoneNumber = validateCodeVO.getPhoneNumber();
        if (StringUtils.isEmpty(phoneNumber) || Objects.isNull(type)) {
            return ResultGenerator.genFailResult("require param phoneNumber and type!");
        }
        if (!ValidateCodeType.contain(type)) {
            return ResultGenerator.genFailResult("invalid validate Code type");
        }
        log.info("[验证码]用户 {} 获取 ‘{}’ 验证码", phoneNumber, ValidateCodeType.getName(type));

        // 短信发送验证码
        String randNum = RndUtils.getValidateCode(6, true);
        return smsUtils.sendMessage(Lists.newArrayList(randNum),phoneNumber,ValidateCodeType.getName(type));
    }

    @PostMapping(value = "/doLogin")
    @ResponseBody
    public Result login(@RequestBody LoginVO loginVO) {
        //校验验证码和手机号是否正确
        String key = KEY_PREFIX_VALIDATE_CODE + ValidateCodeType.REGISTER.getDesc() + ":" + loginVO.getPhoneNumber();
        String randomCode = redisTemplate.opsForValue().get(key);
        if (StringUtils.isEmpty(randomCode) || !randomCode.equals(loginVO.getValidateCode())) {
            return ResultGenerator.genFailResult("登录失败,请检查手机号和验证码!");
        }
        String token = userService.login(loginVO);
        if (token == null) {
            return ResultGenerator.genFailResult("用户名或密码错误");
        }
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);
        return ResultGenerator.genSuccessResult(tokenMap);
    }

    @RequestMapping("/getUser")
    @ResponseBody
    public Result getUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
            String requestTokenHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isEmpty(requestTokenHeader) || !requestTokenHeader.startsWith(JwtTokenUtil.TOKEN_PREFIX)) {
            return ResultGenerator.genUnAuthorizedResult();
        }
        String encryptToken = requestTokenHeader.replace(JwtTokenUtil.TOKEN_PREFIX, "");
        String token = AesEncryptUtils.decrypt(encryptToken, AesEncryptUtils.geneKey(aesSeed));
        String phoneNumber = jwtTokenUtil.getUsernameFromToken(token);
        String key = KEY_PREFIX_USER_STATUS + ":" + phoneNumber;
        String userStatus = redisTemplate.opsForValue().get(key);
        if (StringUtils.isEmpty(userStatus)) {
            log.info("select userStatus from cache is empty,select from db,phoneNumber:{}", phoneNumber);
            UserDTO userDTO = userService.selectByPhoneNumber(phoneNumber);
            redisTemplate.opsForValue().set(key, userDTO.getStatus().toString());
            userStatus = userDTO.getStatus().toString();
        }
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("phoneNumber", phoneNumber);
        userInfo.put("userStatus", userStatus);
        log.info("phoneNumber:{},status:{}", phoneNumber, userStatus);
        return ResultGenerator.genSuccessResult(userInfo);
    }

}
