package com.xkf.cashbook.common.constant;

import lombok.Getter;

/**
 * 用户状态
 *
 * @author xukf01
 */
@Getter
public enum UserStatus {
    /*0->初始;1->激活;-1:封禁'*/
    INIT(0, "初始"),
    ACTIVE(1, "激活"),
    DISABLE(-1, "禁止");

    private Integer status;

    private String desc;

    UserStatus(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }
}
