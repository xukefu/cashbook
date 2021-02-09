package com.xkf.cashbook.web.service.impl;

import com.xkf.cashbook.common.Result;
import com.xkf.cashbook.common.ResultGenerator;
import com.xkf.cashbook.web.mapper.ConsumeCategoryMapper;
import com.xkf.cashbook.web.mysql.ConsumeCategoryDO;
import com.xkf.cashbook.web.service.IConsumeCategoryService;
import com.xkf.cashbook.web.vo.ConsumeCategoryVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ConsumeCategoryServiceImpl implements IConsumeCategoryService {

    @Resource
    private ConsumeCategoryMapper consumeCategoryMapper;

    @Override
    public List<ConsumeCategoryVO> getAll() {
        return consumeCategoryMapper.getAll();
    }

    @Override
    public Result add(String categoryName) {
        int insert = consumeCategoryMapper.insert(new ConsumeCategoryDO(categoryName));
        if (insert == 1) {
            return ResultGenerator.genSuccessResult("添加成功");
        }
        return ResultGenerator.genFailResult("添加失败!");
    }
}
