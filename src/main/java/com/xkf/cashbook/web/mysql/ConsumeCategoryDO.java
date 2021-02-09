package com.xkf.cashbook.web.mysql;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author xukf01
 */
@TableName("c_consume_category")
@Data
public class ConsumeCategoryDO {

    private Long id;

    private String categoryName;

    public ConsumeCategoryDO(String categoryName) {
        this.categoryName = categoryName;
    }
}
