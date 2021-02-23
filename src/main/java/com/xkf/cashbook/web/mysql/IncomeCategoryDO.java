package com.xkf.cashbook.web.mysql;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author xukf01
 */
@TableName("c_income_category")
@Data
public class IncomeCategoryDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String categoryName;

    public IncomeCategoryDO(String categoryName) {
        this.categoryName = categoryName;
    }
}
