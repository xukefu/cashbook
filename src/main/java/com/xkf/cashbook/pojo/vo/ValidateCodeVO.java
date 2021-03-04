package com.xkf.cashbook.pojo.vo;

import lombok.Data;

/**
 * 验证码信息
 *
 * @author xukf01
 */
@Data
public class ValidateCodeVO {

    /**
     * @see com.xkf.cashbook.common.constant.ValidateCodeType;
     */
    private Integer type;

    private String phoneNumber;
}
