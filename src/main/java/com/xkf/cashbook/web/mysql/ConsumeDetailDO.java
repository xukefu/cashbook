package com.xkf.cashbook.web.mysql;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@TableName("c_consume_detail")
@Data
public class ConsumeDetailDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long consumeCategoryId;

    private Double consumeAmount;

    private Integer consumeWay;

    private LocalDateTime recordDate;

    private String recordBy;

    private String consumeBy;

    private String consumeDate;

    private String consumeComment;

    private LocalDateTime updateTime;

}
