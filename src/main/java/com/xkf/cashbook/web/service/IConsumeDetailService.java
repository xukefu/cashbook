package com.xkf.cashbook.web.service;

import com.xkf.cashbook.common.Result;
import com.xkf.cashbook.web.vo.ConsumeDetailVO;

public interface IConsumeDetailService {

    Result addConsumeDetail(ConsumeDetailVO consumeDetailVO);

    Result getLastDetail(int size);
}
