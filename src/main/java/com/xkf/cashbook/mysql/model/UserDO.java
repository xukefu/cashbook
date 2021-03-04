package com.xkf.cashbook.mysql.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户
 *
 * @author xukf01
 */
@Data
@TableName("c_user")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long familyId;

    private String password;

    private String nickName;

    private String phoneNumber;

    private Boolean status;

    private LocalDateTime createTime;

    private String icon;

    private Integer gender;

    private LocalDate birthday;

    private LocalDateTime lastLoginTime;

    private LocalDateTime updateTime;
}
