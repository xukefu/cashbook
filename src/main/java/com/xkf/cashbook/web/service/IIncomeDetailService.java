package com.xkf.cashbook.web.service;

import com.xkf.cashbook.common.Result;
import com.xkf.cashbook.web.dto.IncomeDetailPageDTO;
import com.xkf.cashbook.web.vo.IncomeDetailPageVO;
import com.xkf.cashbook.web.vo.IncomeDetailVO;

public interface IIncomeDetailService {

    Result add(IncomeDetailVO incomeDetailVO);

    IncomeDetailPageDTO pageDetail(IncomeDetailPageVO incomeDetailPageVO);
}
