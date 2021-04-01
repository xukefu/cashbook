package com.xkf.cashbook.pojo.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ConsumeDetailDTO {

    private Long id;

    private Long consumeCategoryId;

    private String consumeCategoryName;

    private Double consumeAmount;

    private Integer consumeWay;

    private LocalDateTime recordDate;

    private Long recordBy;

    private Long consumeBy;

    private LocalDate consumeDate;

    private String consumeComment;

    private String nickName;
}
