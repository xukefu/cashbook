package com.xkf.cashbook.web.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
public class ConsumeDetailPageVO extends ConsumeDetailVO {

    private Long currentPage;

    private Long pageSize;

    private LocalDate consumeStartDate;

    private LocalDate consumeEndDate;

    private Integer consumeDateType;
}
