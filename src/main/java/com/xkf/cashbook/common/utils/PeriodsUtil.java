package com.xkf.cashbook.common.utils;

import com.xkf.cashbook.common.constant.Period;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Objects;

/**
 * 返回时间段
 *
 * @author xukf01
 */
public class PeriodsUtil {

    public static Period getPeriodByType(Integer type) {
        LocalDate today = LocalDate.now();
        LocalDate startDate;
        LocalDate endDate;
        if (Objects.isNull(type)) {
            return null;
        }
        switch (type) {
            //本周
            case 1:
                startDate = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                endDate = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
                break;
            //上周
            case 2:
                startDate = today.minusWeeks(1).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                endDate = today.minusWeeks(1).with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
                break;
            //本月
            case 3:
                startDate = today.with(TemporalAdjusters.firstDayOfMonth());
                endDate = today.with(TemporalAdjusters.lastDayOfMonth());
                break;
            //上月
            case 4:
                startDate = today.minusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
                endDate = today.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
                break;
            //今年
            case 5:
                int year = today.getYear();
                startDate = LocalDate.of(year, 1, 1);
                endDate = LocalDate.of(year, 12, 31);
                break;
            //去年
            case 6:
                year = today.getYear() - 1;
                startDate = LocalDate.of(year, 1, 1);
                endDate = LocalDate.of(year, 12, 31);
                break;
            default:
                return null;
        }
        return new Period(startDate, endDate);
    }
}
