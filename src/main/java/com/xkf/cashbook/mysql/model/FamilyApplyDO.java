package com.xkf.cashbook.mysql.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author xukf01
 */
@Data
@TableName("c_family_apply")
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class FamilyApplyDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long applyFamilyId;

    private String applyFamilyName;

    private String applyPhoneNumber;

    private Integer applyStatus;

    private Long applyUserId;

    private LocalDateTime applyTime;

    private LocalDateTime approveTime;

    private String approveCode;
}
