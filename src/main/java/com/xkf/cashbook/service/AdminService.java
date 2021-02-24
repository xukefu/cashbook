package com.xkf.cashbook.service;

import com.xkf.cashbook.common.result.Result;
import com.xkf.cashbook.pojo.vo.TableVO;

public interface AdminService {

    TableVO getSumTotalTableByCategory();

    Result getMonthlyConsumeAmount();

    Result getWeeklyConsumeAmount();

    Result getMonthlyIncomeAmount();

    Result getMonthlyConsumeProportion();

    Result getEveryDayConsumeAmount(int days);
}
