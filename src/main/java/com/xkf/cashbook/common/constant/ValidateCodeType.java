package com.xkf.cashbook.common.constant;

import lombok.Getter;

/**
 * 验证码类型
 *
 * @author xukf01
 */
@Getter
@SuppressWarnings("all")
public enum ValidateCodeType {

    REGISTER(0,"register"),
    LOGIN(1,"login"),
    FORGET_PASSWORD(2,"forget_password");

    private Integer type;

    private String desc;

    ValidateCodeType(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static String getName(int type){
        for (ValidateCodeType a: ValidateCodeType.values()){
            if (a.type == type){
                return a.desc;
            }
        }
        return "error";
    }

    /**
     * 是否包含
     * @param type
     * @return
     */
    public static boolean contain(int type){
        for (ValidateCodeType a: ValidateCodeType.values()){
            if (a.type == type){
                return true;
            }
        }
        return false;
    }
}
