package com.xkf.cashbook.pojo.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户
 *
 * @author xukf01
 */
@Data
@Accessors(chain = true)
public class UserDTO {

    private Long id;

    private Long familyId;

    private String nickName;

    private String phoneNumber;

    private LocalDateTime createTime;

    private String icon;

    private Integer gender;

    private LocalDate birthday;

    private LocalDateTime lastLoginTime;

    private LocalDateTime updateTime;

    private Integer status;
}
