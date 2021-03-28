package com.xkf.cashbook.pojo.dto;

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

    private Long recordBy;

    private Long consumeBy;

    private String consumeDate;

    private String consumeComment;

    private String nickName;
}
