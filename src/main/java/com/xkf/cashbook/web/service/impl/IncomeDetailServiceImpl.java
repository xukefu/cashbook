package com.xkf.cashbook.web.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.xkf.cashbook.common.Result;
import com.xkf.cashbook.common.ResultGenerator;
import com.xkf.cashbook.web.mapper.IncomeDetailMapper;
import com.xkf.cashbook.web.mysql.IncomeDetailDO;
import com.xkf.cashbook.web.service.IIncomeDetailService;
import com.xkf.cashbook.web.vo.IncomeDetailVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author xukf01
 */
@Service
public class IncomeDetailServiceImpl implements IIncomeDetailService {
    @Resource
    private IncomeDetailMapper incomeDetailMapper;

    @Override
    @Transactional
    public Result add(IncomeDetailVO incomeDetailVO) {
        incomeDetailVO.setRecordBy(incomeDetailVO.getIncomeBy().toString());
        incomeDetailVO.setRecordDate(new Date());
        IncomeDetailDO incomeDetailDO = BeanUtil.copyProperties(incomeDetailVO, IncomeDetailDO.class);
        int add = incomeDetailMapper.insert(incomeDetailDO);
        if (add == 1) {
            return ResultGenerator.genSuccessResult("记录成功!", null);
        }
        return ResultGenerator.genFailResult();
    }
}
