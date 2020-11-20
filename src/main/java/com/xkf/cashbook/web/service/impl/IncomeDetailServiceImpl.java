package com.xkf.cashbook.web.service.impl;

import com.xkf.cashbook.common.Result;
import com.xkf.cashbook.common.ResultGenerator;
import com.xkf.cashbook.web.mapper.IncomeDetailMapper;
import com.xkf.cashbook.web.service.IIncomeDetailService;
import com.xkf.cashbook.web.vo.IncomeDetailVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class IncomeDetailServiceImpl implements IIncomeDetailService {
    @Resource
    private IncomeDetailMapper incomeDetailMapper;

    @Override
    @Transactional
    public Result add(IncomeDetailVO incomeDetailVO) {
        incomeDetailVO.setRecordBy(incomeDetailVO.getIncomeBy().toString());
        incomeDetailVO.setRecordDate(new Date());
        int add = incomeDetailMapper.add(incomeDetailVO);
        if (add == 1) {
            return ResultGenerator.genSuccessResult("记录成功!", null);
        }
        return ResultGenerator.genFailResult();
    }
}
