package com.xkf.cashbook.mysql.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author xukf01
 */
@Data
@TableName("c_family_category")
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class FamilyCategoryDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long familyId;

    private Long categoryId;

    private Integer categoryType;
}
