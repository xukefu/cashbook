package com.xkf.cashbook.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/** 支出dto
 * @author xukf01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsumeDetailPageDTO {

    private Long total;

    private List<ConsumeDetailDTO> consumeDetails;

}