package com.xkf.cashbook.web.service;

import com.xkf.cashbook.common.Result;
import com.xkf.cashbook.web.vo.TableVO;

public interface AdminService {

    TableVO getSumTotalTableByCategory();

    Result getMonthlyConsumeAmount();

    Result getWeeklyConsumeAmount();

    Result getMonthlyIncomeAmount();

    Result getMonthlyConsumeProportion();

    Result getEveryDayConsumeAmount(int days);
}
