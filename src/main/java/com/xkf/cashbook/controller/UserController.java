package com.xkf.cashbook.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.xkf.cashbook.common.constant.ValidateCodeType;
import com.xkf.cashbook.common.result.Result;
import com.xkf.cashbook.common.result.ResultGenerator;
import com.xkf.cashbook.common.utils.AesEncryptUtils;
import com.xkf.cashbook.common.utils.RndUtils;
import com.xkf.cashbook.common.utils.SmsUtils;
import com.xkf.cashbook.jwt.JwtTokenUtil;
import com.xkf.cashbook.mysql.model.FamilyDO;
import com.xkf.cashbook.mysql.model.UserDO;
import com.xkf.cashbook.pojo.dto.UserDTO;
import com.xkf.cashbook.pojo.dto.UserIndexDTO;
import com.xkf.cashbook.pojo.dto.UserSelectDTO;
import com.xkf.cashbook.pojo.vo.LoginVO;
import com.xkf.cashbook.pojo.vo.ValidateCodeVO;
import com.xkf.cashbook.service.IFamilyService;
import com.xkf.cashbook.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static com.xkf.cashbook.common.constant.Constants.*;

/**
 * @author xukf01
 */
@Controller
@RequestMapping("/user")
@Slf4j
public class UserController extends BaseController {

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

    @Resource
    private IFamilyService familyService;

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
            return ResultGenerator.genFailResult("参数有误");
        }
        Pattern pattern = Pattern.compile("^[1]\\d{10}$");
        if (!pattern.matcher(phoneNumber).matches()) {
            return ResultGenerator.genFailResult("手机号格式错误");
        }
        if (!ValidateCodeType.REGISTER.getType().equals(type)) {
            return ResultGenerator.genFailResult("验证码类型错误");
        }
        log.info("[验证码]用户:{} 获取:{} 类型的验证码", phoneNumber, ValidateCodeType.getName(type));

        // 短信发送验证码
        String randNum = RndUtils.getValidateCode(6, true);
        return smsUtils.sendMessage(Lists.newArrayList(randNum), phoneNumber, ValidateCodeType.getName(type));
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
    public Result getUser(HttpServletRequest request) throws Exception {
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

    @GetMapping("logout")
    @ResponseBody
    public Result logout(HttpServletRequest request) {
        String cellphone = getPhoneNumber(request);
        log.info("[登出] 手机号：{}", cellphone);
        String token = decryptJwtToken(request);
        // 黑名单
        String keyBlacklist = KEY_PREFIX_TOKEN_BLACKLIST + cellphone + ":" + Md5Crypt.md5Crypt(token.getBytes());
        redisTemplate.opsForValue().set(keyBlacklist, token, JwtTokenUtil.JWT_REFRESH_TOKEN_VALIDITY, TimeUnit.SECONDS);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("getUserInfo")
    @ResponseBody
    public Result getUserInfo(HttpServletRequest request) {
        String phoneNumber = getPhoneNumber(request);
        if (StringUtils.isEmpty(phoneNumber)) {
            return ResultGenerator.genFailResult("用户信息异常,请尝试退出重新登录");
        }
        UserDTO userDTO = userService.selectByPhoneNumber(phoneNumber);
        if (Objects.isNull(userDTO)) {
            return ResultGenerator.genFailResult("用户信息异常,请尝试退出重新登录");
        }
        UserIndexDTO userIndexDTO = new UserIndexDTO();
        userIndexDTO.setUserNickName(userDTO.getNickName());

        FamilyDO familyDO = familyService.getById(userDTO.getFamilyId());
        if (Objects.isNull(familyDO)) {
            return ResultGenerator.genFailResult("您还没创建/加入任何家庭哦");
        }
        userIndexDTO.setFamilyName(familyDO.getFamilyName());
        userIndexDTO.setFamilyCreateTime(familyDO.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        List<UserSelectDTO> familyUsers = userService.selectUsersByFamilyId(familyDO.getId());
        if (CollectionUtils.isEmpty(familyUsers)) {
            return ResultGenerator.genFailResult("用户信息异常,请尝试退出重新登录");
        }
        userIndexDTO.setFamilyUserNumbers(familyUsers.size());
        return ResultGenerator.genSuccessResult("", userIndexDTO);
    }


}
