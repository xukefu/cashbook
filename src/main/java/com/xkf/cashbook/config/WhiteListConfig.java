package com.xkf.cashbook.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/** 白名单配置
 * @author xukf01
 */
@Component
@ConfigurationProperties(prefix = "jwt")
public class WhiteListConfig {

    private String[] whiteList;

    public String[] getWhiteList() {
        return whiteList;
    }

    public void setWhiteList(String[] whiteList) {
        this.whiteList = whiteList;
    }
}
