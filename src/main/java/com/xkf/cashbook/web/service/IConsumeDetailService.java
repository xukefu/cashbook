package com.xkf.cashbook.web.service;

import com.xkf.cashbook.common.Result;
import com.xkf.cashbook.web.dto.ConsumeDetailPageDTO;
import com.xkf.cashbook.web.vo.ConsumeDetailPageVO;
import com.xkf.cashbook.web.vo.ConsumeDetailVO;

public interface IConsumeDetailService {

    Result addConsumeDetail(ConsumeDetailVO consumeDetailVO);

    Result getLastDetail(int size);

    /** 消费详情分页接口
     * @param consumeDetailPageVO 消费详情
     * @return
     */
    ConsumeDetailPageDTO pageDetail(ConsumeDetailPageVO consumeDetailPageVO);
}
