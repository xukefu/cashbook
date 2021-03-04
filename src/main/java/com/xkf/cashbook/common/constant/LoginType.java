package com.xkf.cashbook.common.constant;

import lombok.Getter;

@Getter
@SuppressWarnings("all")
public enum LoginType {

    PASSWORD(0,"password"),
    VALIDATE_CODE(1,"validateCode");

    private Integer type;

    private String desc;

    LoginType(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static String getName(int type){
        for (LoginType a: LoginType.values()){
            if (a.type == type){
                return a.desc;
            }
        }
        return "error";
    }
}
