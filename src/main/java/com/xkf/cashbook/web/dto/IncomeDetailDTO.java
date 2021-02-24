package com.xkf.cashbook.web.dto;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class IncomeDetailDTO {

    private Long id;

    private Integer incomeBy;

    private Double incomeAmount;

    private Long incomeCategoryId;

    private String incomeDate;

    private LocalDateTime recordDate;

    private Integer recordBy;

    private String incomeComment;

    private String incomeCategoryName;
}
