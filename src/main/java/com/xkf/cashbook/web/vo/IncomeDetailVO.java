package com.xkf.cashbook.web.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author xukf01
 */
@Data
public class IncomeDetailVO {

    private Long id;

    private Integer incomeBy;

    private Double incomeAmount;

    private Long incomeCategoryId;

    private String incomeDate;

    private LocalDateTime recordDate;

    private Integer recordBy;

    private String incomeComment;
}
