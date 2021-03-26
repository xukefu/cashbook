package com.xkf.cashbook.pojo.vo;


import lombok.Data;

import java.time.LocalDateTime;

/** 家庭
 * @author xukf01
 */
@Data
public class FamilyVO {

    private Integer id;

    private String familyName;

    private String familyOwner;

    private String familyIcon;

    private String note;

    private LocalDateTime createTime;

    private Integer status;

    private String userNickName;
}
