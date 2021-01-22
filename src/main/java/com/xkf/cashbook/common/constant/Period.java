package com.xkf.cashbook.common.constant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Period {
    private LocalDate startDate;

    private LocalDate endDate;
}
