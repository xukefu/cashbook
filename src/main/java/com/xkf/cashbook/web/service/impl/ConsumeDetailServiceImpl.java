package com.xkf.cashbook.web.service.impl;

import com.xkf.cashbook.common.Result;
import com.xkf.cashbook.common.ResultGenerator;
import com.xkf.cashbook.web.mapper.ConsumeCategoryMapper;
import com.xkf.cashbook.web.mapper.ConsumeDetailMapper;
import com.xkf.cashbook.web.service.IConsumeDetailService;
import com.xkf.cashbook.web.vo.ConsumeDetailVO;
import com.xkf.cashbook.web.vo.ConsumeReportVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class ConsumeDetailServiceImpl implements IConsumeDetailService {

    @Resource
    private ConsumeDetailMapper consumeDetailMapper;

    @Resource
    private ConsumeCategoryMapper consumeCategoryMapper;

    @Override
    @Transactional
    public Result addConsumeDetail(ConsumeDetailVO consumeDetailVO) {
        consumeDetailVO.setRecordDate(new Date());
        consumeDetailVO.setRecordBy(consumeDetailVO.getConsumeBy());
        int add = consumeDetailMapper.add(consumeDetailVO);
        if (add == 1) {
            return ResultGenerator.genSuccessResult("记录成功!",null);
        }
        return ResultGenerator.genFailResult();
    }

    @Override
    public Result getLastDetail(int size) {
        List<ConsumeDetailVO> consumeDetails = consumeDetailMapper.getLastDetail(size);
        if (!CollectionUtils.isEmpty(consumeDetails)) {
            for (ConsumeDetailVO consumeDetailVO : consumeDetails) {
                String categoryName = consumeCategoryMapper.getCategoryNameById(consumeDetailVO.getConsumeCategoryId());
                consumeDetailVO.setConsumeCategoryName(categoryName);
            }
        }
        return ResultGenerator.genSuccessResult(consumeDetails);
    }
}
