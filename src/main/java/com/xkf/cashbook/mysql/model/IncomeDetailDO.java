package com.xkf.cashbook.mysql.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;


/**
 * @author xukf01
 */
@Data
@TableName("c_income_detail")
public class IncomeDetailDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Integer incomeBy;

    private Double incomeAmount;

    private Long incomeCategoryId;

    private String incomeDate;

    private LocalDateTime recordDate;

    private Integer recordBy;

    private String incomeComment;

    private LocalDateTime updateTime;
}
