package com.xkf.cashbook.web.service.impl;

import com.xkf.cashbook.common.Result;
import com.xkf.cashbook.common.ResultGenerator;
import com.xkf.cashbook.web.mapper.IncomeCategoryMapper;
import com.xkf.cashbook.web.service.IncomeCategoryService;
import com.xkf.cashbook.web.vo.IncomeCategoryVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class IncomeCategoryServiceImpl implements IncomeCategoryService {
    @Resource
    private IncomeCategoryMapper incomeCategoryMapper;

    @Override
    public Result getAll() {
        List<IncomeCategoryVO> incomeCategorys = incomeCategoryMapper.getAll();
        return ResultGenerator.genSuccessResult(incomeCategorys);
    }
}
