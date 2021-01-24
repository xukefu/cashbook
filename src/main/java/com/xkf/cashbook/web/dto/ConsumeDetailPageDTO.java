package com.xkf.cashbook.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsumeDetailPageDTO {

    private long total;

    private List<ConsumeDetailDTO> consumeDetails;

    /*private BigDecimal totalConsumeAmount;

    private BigDecimal subtotalConsumeAmount;*/

}