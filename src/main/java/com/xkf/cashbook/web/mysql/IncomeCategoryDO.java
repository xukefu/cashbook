package com.xkf.cashbook.web.mysql;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author xukf01
 */
@TableName("c_income_category")
@Data
public class IncomeCategoryDO {

    private Long id;

    private String categoryName;

    public IncomeCategoryDO(String categoryName) {
        this.categoryName = categoryName;
    }
}
