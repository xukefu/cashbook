package com.xkf.cashbook.web.vo;

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

    private String recordBy;

    private String consumeBy;

    private String consumeDate;

    private String consumeCategoryName;


}
