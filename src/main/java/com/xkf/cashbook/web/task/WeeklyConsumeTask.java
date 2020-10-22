package com.xkf.cashbook.web.task;

import cn.ucloud.common.pojo.Account;
import cn.ucloud.usms.client.DefaultUSMSClient;
import cn.ucloud.usms.client.USMSClient;
import cn.ucloud.usms.model.SendUSMSMessageParam;
import cn.ucloud.usms.pojo.USMSConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xukf01
 */
@Configuration
@EnableScheduling
public class WeeklyConsumeTask {

    private USMSClient client;

    private SendUSMSMessageParam param;

    private void init(){
        client = new DefaultUSMSClient(new USMSConfig(
                new Account("uOPzPcjIr83NJ0CKBfMFO9haQruFvKZZEfZGPgtwJRqGHwLJGdnvhY27JxXqaHZKY",
                        "A8Xz9j0Js8W5wid3DPmYECEcFqYve8NhT3mN453pX")));
        List<String> phoneNumbers = new ArrayList<>();
        phoneNumbers.add("18823748161");
        String templateId = System.getenv("USMSTemplateId");
        param = new SendUSMSMessageParam(phoneNumbers, templateId);
        param.setSigContent(System.getenv("USMSSigId"));
        param.setProjectId(System.getenv("ProjectId"));
    }


    /*@Scheduled(cron = "0 0 21 0 0 7 ?")
    private void sendWeeklySummaryMessage(){

    }
*/


}
