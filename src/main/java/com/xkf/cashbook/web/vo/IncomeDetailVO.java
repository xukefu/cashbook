package com.xkf.cashbook.web.vo;

import lombok.Data;

import java.util.Date;

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

    private Date recordDate;

    private String recordBy;

    private String incomeComment;
}
