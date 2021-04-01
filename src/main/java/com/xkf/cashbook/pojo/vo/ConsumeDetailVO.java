package com.xkf.cashbook.pojo.vo;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 消费详情
 *
 * @author xukf01
 */
@Data
public class ConsumeDetailVO {

    private Long id;

    private Long consumeCategoryId;

    private Double consumeAmount;

    private Integer consumeWay;

    private LocalDateTime recordDate;

    private Long recordBy;

    private Long consumeBy;

    private LocalDate consumeDate;

    private String consumeCategoryName;

    private String consumeComment;

}
