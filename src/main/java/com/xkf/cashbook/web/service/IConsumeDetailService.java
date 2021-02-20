package com.xkf.cashbook.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xkf.cashbook.common.Result;
import com.xkf.cashbook.web.dto.ConsumeDetailPageDTO;
import com.xkf.cashbook.web.mysql.ConsumeDetailDO;
import com.xkf.cashbook.web.vo.ConsumeDetailPageVO;
import com.xkf.cashbook.web.vo.ConsumeDetailVO;

public interface IConsumeDetailService extends IService<ConsumeDetailDO> {

    Result getLastDetail(int size);

    /** 消费详情分页接口
     * @param consumeDetailPageVO 消费详情
     * @return
     */
    ConsumeDetailPageDTO pageDetail(ConsumeDetailPageVO consumeDetailPageVO);
}
