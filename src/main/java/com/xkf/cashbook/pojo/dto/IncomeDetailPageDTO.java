package com.xkf.cashbook.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/** 收入dto
 * @author xukf01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncomeDetailPageDTO {

    private Long total;

    private List<IncomeDetailDTO> incomeDetails;
}
