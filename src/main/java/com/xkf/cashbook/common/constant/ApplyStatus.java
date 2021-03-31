package com.xkf.cashbook.common.constant;

import lombok.Getter;

/**
 * 用户状态
 *
 * @author xukf01
 */
@Getter
public enum ApplyStatus {
    /*0->初始;1->激活;-1:封禁'*/
    INIT(0, "初始"),
    IS_READ(1, "已读"),
    ACTIVE(2, "同意"),
    DISABLE(3, "拒绝");

    private Integer status;

    private String desc;

    ApplyStatus(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }
}
