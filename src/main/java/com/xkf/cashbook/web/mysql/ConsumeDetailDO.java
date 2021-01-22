package com.xkf.cashbook.web.mysql;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@TableName("c_consume_detail")
@Data
public class ConsumeDetailDO {

    private Long id;

    private Long consumeCategoryId;

    private Double consumeAmount;

    private Integer consumeWay;

    private Date recordDate;

    private String recordBy;

    private String consumeBy;

    private String consumeDate;

}
