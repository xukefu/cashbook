package com.xkf.cashbook.pojo.vo;


import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

/**
 * 申请家庭
 *
 * @author xukf01
 */
@Data
public class FamilyApplyVO {

    private Long id;

    private Long applyFamilyId;

    @NotEmpty(message = "家庭名称不能为空")
    private String applyFamilyName;

    private Integer applyStatus;

    private Long applyUserId;

    private String applyPhoneNumber;

    private LocalDateTime applyTime;

    private LocalDateTime approveTime;

    private String userNickName;


}
