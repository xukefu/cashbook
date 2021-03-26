package com.xkf.cashbook.pojo.dto;


import lombok.Data;

import java.time.LocalDateTime;

/**
 * 收入详情
 *
 * @author xukf01
 */
@Data
public class IncomeDetailDTO {

    private Long id;

    private Long incomeBy;

    private Double incomeAmount;

    private Long incomeCategoryId;

    private String incomeDate;

    private LocalDateTime recordDate;

    private Long recordBy;

    private String incomeComment;

    private String incomeCategoryName;
}
