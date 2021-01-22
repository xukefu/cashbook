package com.xkf.cashbook.common.constant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.TemporalAdjusters;

/**
 * @author xukf01
 */
@SuppressWarnings("all")
public enum PeriodsType {

    CURRENT_WEEK(1, "本周"),
    LAST_WEEK(2, "上周"),
    CURRENT_MONTH(3, "本月"),
    LAST_MONTH(4, "上个月"),
    LAST_YEAR(5, "今年"),
    CURRENT_YEAR(6, "去年");

    private Integer type;

    private String desc;

    PeriodsType(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

}
