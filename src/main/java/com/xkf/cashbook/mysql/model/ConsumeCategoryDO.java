package com.xkf.cashbook.mysql.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author xukf01
 */
@TableName("c_consume_category")
@Data
public class ConsumeCategoryDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String categoryName;

    public ConsumeCategoryDO(String categoryName) {
        this.categoryName = categoryName;
    }
}
