package com.xkf.cashbook.pojo.vo;

import lombok.Data;

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

    private Date recordDate;

    private Long recordBy;

    private Long consumeBy;

    private String consumeDate;

    private String consumeCategoryName;

    private String consumeComment;

}
