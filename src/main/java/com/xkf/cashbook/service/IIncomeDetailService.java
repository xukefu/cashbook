package com.xkf.cashbook.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xkf.cashbook.common.result.Result;
import com.xkf.cashbook.pojo.dto.IncomeDetailPageDTO;
import com.xkf.cashbook.mysql.model.IncomeDetailDO;
import com.xkf.cashbook.pojo.vo.IncomeDetailPageVO;
import com.xkf.cashbook.pojo.vo.IncomeDetailVO;

public interface IIncomeDetailService extends IService<IncomeDetailDO> {

    Result add(IncomeDetailVO incomeDetailVO);

    IncomeDetailPageDTO pageDetail(IncomeDetailPageVO incomeDetailPageVO);
}
