package com.xkf.cashbook.web.dto;


import lombok.Data;

import java.time.LocalDate;

@Data
public class IncomeDetailDTO {

    private Long id;

    private Integer incomeBy;

    private Double incomeAmount;

    private Long incomeCategoryId;

    private String incomeDate;

    private LocalDate recordDate;

    private String recordBy;

    private String incomeComment;

    private String incomeCategoryName;
}
