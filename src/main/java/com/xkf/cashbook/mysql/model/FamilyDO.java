package com.xkf.cashbook.mysql.model;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/** 家庭
 * @author xukf01
 */
@Data
@TableName("c_family")
public class FamilyDO {

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    private String familyName;

    private String familyOwner;

    private String familyIcon;

    private String note;

    private LocalDateTime createTime;

    private Integer status;
}
