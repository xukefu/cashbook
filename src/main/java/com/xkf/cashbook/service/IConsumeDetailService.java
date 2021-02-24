package com.xkf.cashbook.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xkf.cashbook.common.result.Result;
import com.xkf.cashbook.pojo.dto.ConsumeDetailPageDTO;
import com.xkf.cashbook.mysql.model.ConsumeDetailDO;
import com.xkf.cashbook.pojo.vo.ConsumeDetailPageVO;
import com.xkf.cashbook.pojo.vo.ConsumeDetailVO;

public interface IConsumeDetailService extends IService<ConsumeDetailDO> {

    Result getLastDetail(int size);

    /** 消费详情分页接口
     * @param consumeDetailPageVO 消费详情
     * @return
     */
    ConsumeDetailPageDTO pageDetail(ConsumeDetailPageVO consumeDetailPageVO);
}
