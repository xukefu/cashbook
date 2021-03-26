package com.xkf.cashbook.pojo.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author xukf01
 */
@Data
public class ConsumeCategoryDTO {

    private Long id;

    private String categoryName;

    public ConsumeCategoryDTO(String categoryName) {
        this.categoryName = categoryName;
    }
}
