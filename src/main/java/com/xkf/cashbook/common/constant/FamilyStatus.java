package com.xkf.cashbook.common.constant;

import lombok.Getter;

/**
 * 用户状态
 *
 * @author xukf01
 */
@Getter
public enum FamilyStatus {
    /*0->初始;1->禁用;*/
    INIT(0, "初始"),
    ACTIVE(1, "禁用");

    private Integer status;

    private String desc;

    FamilyStatus(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }
}
