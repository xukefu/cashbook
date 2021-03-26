package com.xkf.cashbook.common.utils;

import cn.ucloud.usms.client.DefaultUSMSClient;
import cn.ucloud.usms.model.SendUSMSMessageParam;
import com.google.common.collect.Lists;
import com.xkf.cashbook.common.result.Result;
import com.xkf.cashbook.common.result.ResultGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.xkf.cashbook.common.constant.Constants.KEY_PREFIX_VALIDATE_CODE;
import static com.xkf.cashbook.common.constant.Constants.VALIDATE_CODE_EXPIRE_MINUTES;

/**
 * 短信工具
 *
 * @author xukf01
 */
@Slf4j
@Component
public class SmsUtils {

    @Resource
    private DefaultUSMSClient usmsClient;

    @Value("${usms.validate.templateId}")
    private static String validCodeTemplateId;

    @Value("${usms.applyFamily.templateId}")
    private static String applyFamilyTemplateId;

    @Value("${usms.validate.sigId}")
    private static String sigId;

    @Autowired
    private StringRedisTemplate redisTemplate;

    public Result sendMessage(List<String> messages, String phoneNumber, String validCodeType) throws Exception {
        // 保存验证码
        String key = KEY_PREFIX_VALIDATE_CODE + validCodeType + ":" + phoneNumber;
        // 限制 90s 重发频率
        if (redisTemplate.hasKey(key)) {
            long resendTime = VALIDATE_CODE_EXPIRE_MINUTES * 60 - 90;
            long expire = redisTemplate.getExpire(key, TimeUnit.SECONDS) - resendTime;
            if (expire > 0) {
                return ResultGenerator.genFailResult(String.format("请 %d 秒后获取验证码", expire));
            }
        }
        redisTemplate.opsForValue().set(key, messages.get(0), VALIDATE_CODE_EXPIRE_MINUTES, TimeUnit.MINUTES);
        log.info("验证码已存redis;keyName:{},value:{}", key, redisTemplate.opsForValue().get(key));
        SendUSMSMessageParam messageParam = new SendUSMSMessageParam(Lists.newArrayList(phoneNumber), validCodeTemplateId);
        messageParam.setTemplateParams(messages);
        messageParam.setSigContent(sigId);
//        SendUSMSMessageResult sendUSMSMessageResult = usmsClient.sendUSMSMessage(messageParam);
//        log.info("短信发送:phoneNumber:{},randNum:{},result:{}", phoneNumber, randNum, sendUSMSMessageResult.getMessage());
        log.info("短信模拟发送:phoneNumber:{},randNum:{}", phoneNumber, messages.get(0));
        return ResultGenerator.genSuccessResult();
    }

    public Result sendFamilyApply(List<String> messages, String phoneNumber) throws Exception {
        log.info("sendFamilyApply:{}", messages.get(0));
        SendUSMSMessageParam messageParam = new SendUSMSMessageParam(Lists.newArrayList(phoneNumber), applyFamilyTemplateId);
        messageParam.setTemplateParams(messages);
        messageParam.setSigContent(sigId);
//        SendUSMSMessageResult sendUSMSMessageResult = usmsClient.sendUSMSMessage(messageParam);
//        log.info("短信发送:phoneNumber:{},randNum:{},result:{}", phoneNumber, randNum, sendUSMSMessageResult.getMessage());
        log.info("短信模拟发送:phoneNumber:{},url:{}", phoneNumber, messages.get(0));
        return ResultGenerator.genSuccessResult();
    }

}
