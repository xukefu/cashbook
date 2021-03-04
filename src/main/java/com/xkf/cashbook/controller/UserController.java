package com.xkf.cashbook.controller;

import cn.ucloud.common.pojo.Account;
import cn.ucloud.common.pojo.UcloudConfig;
import cn.ucloud.usms.client.DefaultUSMSClient;
import cn.ucloud.usms.model.SendUSMSMessageParam;
import cn.ucloud.usms.model.SendUSMSMessageResult;
import com.google.common.collect.Lists;
import com.xkf.cashbook.common.constant.ValidateCodeType;
import com.xkf.cashbook.common.result.Result;
import com.xkf.cashbook.common.result.ResultGenerator;
import com.xkf.cashbook.common.utils.RndUtils;
import com.xkf.cashbook.pojo.vo.LoginVO;
import com.xkf.cashbook.pojo.vo.ValidateCodeVO;
import com.xkf.cashbook.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.xkf.cashbook.common.constant.Constants.*;

@Controller
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    protected StringRedisTemplate redisTemplate;

    @Resource
    private Account account;

    @Value("${usms.validate.templateId}")
    private String templateId;

    @Value("${usms.validate.sigId}")
    private String sigId;

    @Resource
    private UserService userService;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @GetMapping("/login")
    public String login(){
        return "user/login";
    }

    @PostMapping("/sendValidateCode")
    @ResponseBody
    public Result sendValidateCode(@RequestBody ValidateCodeVO validateCodeVO) throws IOException {
        Integer type = validateCodeVO.getType();
        String cellphone = validateCodeVO.getPhoneNumber();
        if (StringUtils.isEmpty(cellphone) || Objects.isNull(type)) {
            return ResultGenerator.genFailResult("require param cellphone and type!");
        }

        if (!ValidateCodeType.contain(type)) {
            return ResultGenerator.genFailResult("invalid validate Code type");
        }

        log.info("[验证码]用户 {} 获取 ‘{}’ 验证码", cellphone, ValidateCodeType.getName(type));

        String key = KEY_PREFIX_VALIDATE_CODE + ValidateCodeType.getName(type) + ":" + cellphone;
        // 限制 90s 重发频率
        if (redisTemplate.hasKey(key)) {
            long resendTime = VALIDATE_CODE_EXPIRE_MINUTES * 60 - 90;
            long expire = redisTemplate.getExpire(key, TimeUnit.SECONDS) - resendTime;
            if (expire > 0) {
                return ResultGenerator.genFailResult(String.format("请 %d 秒后获取验证码", expire));
            }
        }
        // 短信发送验证码
        send(key, VALIDATE_CODE_EXPIRE_MINUTES, cellphone);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 发送短信验证码
     * @param keyName
     * @param expireMinutes 过期时间
     * @param cellphone
     * @return
     */
    private void send(String keyName, long expireMinutes, String cellphone){
        String randNum = RndUtils.getValidateCode(6, true);
        // 保存验证码
        redisTemplate.opsForValue().set(keyName, randNum, expireMinutes, TimeUnit.MINUTES);
        log.info("验证码已存redis;keyName:{},value:{}",keyName,redisTemplate.opsForValue().get(keyName));
        // 发送短信
        try {
            DefaultUSMSClient usmsClient = new DefaultUSMSClient(new UcloudConfig(account));
            SendUSMSMessageParam messageParam = new SendUSMSMessageParam(Lists.newArrayList(cellphone),templateId);
            messageParam.setTemplateParams(Lists.newArrayList(randNum));
            messageParam.setSigContent(sigId);
//            SendUSMSMessageResult sendUSMSMessageResult = usmsClient.sendUSMSMessage(messageParam);
//            log.info("短信发送:cellphone:{},randNum:{},result:{}",cellphone,randNum,sendUSMSMessageResult.getMessage());
            log.info("短信模拟发送:cellphone:{},randNum:{}",cellphone,randNum);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("短信发送失败,cellphone:{}",cellphone,e);
        }
    }

    @PostMapping(value = "/doLogin")
    @ResponseBody
    public Result login(@RequestBody LoginVO loginVO) {
        String token = userService.login(loginVO);
        if (token == null) {
            return ResultGenerator.genFailResult("用户名或密码错误");
        }
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);
        return ResultGenerator.genSuccessResult(tokenMap);
    }
}
