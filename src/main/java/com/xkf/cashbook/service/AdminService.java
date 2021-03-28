package com.xkf.cashbook.service;

import com.xkf.cashbook.common.result.Result;
import com.xkf.cashbook.pojo.vo.TableVO;

public interface AdminService {

    TableVO getSumTotalTableByCategory(Long familyId);

    Result getMonthlyConsumeAmount(Long familyId);

    Result getWeeklyConsumeAmount(Long familyId);

    Result getMonthlyIncomeAmount(Long familyId);

    Result getMonthlyConsumeProportion(Long familyId);

    Result getEveryDayConsumeAmount(int days,Long familyId);
}
