package com.xkf.cashbook.common.constant;

import lombok.Getter;

/**
 * 分类类型
 *
 * @author xukf01
 */
@Getter
public enum FamilyCategoryType {
    CONSUME(1, "消费"),
    INCOME(2, "收入");

    private Integer type;

    private String desc;

    FamilyCategoryType(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }
}
