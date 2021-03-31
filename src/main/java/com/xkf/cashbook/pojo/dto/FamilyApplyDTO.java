package com.xkf.cashbook.pojo.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author xukf01
 */
@Data
public class FamilyApplyDTO {

    private Long applyFamilyId;

    private String applyFamilyName;

    private String applyPhoneNumber;

    private Integer applyStatus;

    private Long applyUserId;

    private String approveCode;

    private String applyTime;

    private String applyUserNickName;

    private boolean isFamilyOwner;
}
