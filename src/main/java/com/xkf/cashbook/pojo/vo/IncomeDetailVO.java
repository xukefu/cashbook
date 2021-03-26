package com.xkf.cashbook.pojo.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author xukf01
 */
@Data
public class IncomeDetailVO {

    private Long id;

    private Long incomeBy;

    private Double incomeAmount;

    private Long incomeCategoryId;

    private String incomeDate;

    private LocalDateTime recordDate;

    private Long recordBy;

    private String incomeComment;
}
