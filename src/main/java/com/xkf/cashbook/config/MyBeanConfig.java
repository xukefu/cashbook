package com.xkf.cashbook.config;

import cn.ucloud.common.pojo.Account;
import com.xkf.cashbook.CashBookApplication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = CashBookApplication.class)
public class MyBeanConfig {

    @Value("${usms.privateKey}")
    private String privateKey;

    @Value("${usms.publicKey}")
    private String publicKey;

    @Bean
    public Account getAccount(){
        return new Account(privateKey,publicKey);
    }

}
