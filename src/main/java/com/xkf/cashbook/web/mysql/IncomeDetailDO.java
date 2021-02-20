package com.xkf.cashbook.web.mysql;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author xukf01
 */
@Data
@TableName("c_income_detail")
public class IncomeDetailDO {
    private Long id;

    private Integer incomeBy;

    private Double incomeAmount;

    private Long incomeCategoryId;

    private String incomeDate;

    private Date recordDate;

    private String recordBy;

    private String incomeComment;
}
