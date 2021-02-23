package com.xkf.cashbook.web.mysql;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;

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

    private LocalDate recordDate;

    private String recordBy;

    private String incomeComment;
}
