package com.xkf.cashbook.web.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ConsumeDetailDTO {

    private Long id;

    private Long consumeCategoryId;

    private String consumeCategoryName;

    private Double consumeAmount;

    private Integer consumeWay;

    private Date recordDate;

    private String recordBy;

    private String consumeBy;

    private String consumeDate;

    private String consumeComment;
}
