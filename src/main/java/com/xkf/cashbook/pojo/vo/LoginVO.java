package com.xkf.cashbook.pojo.vo;

import lombok.Data;

@Data
public class LoginVO {

    private String phoneNumber;

    private String password;

    private Integer loginType;

    private String validateCode;
}
