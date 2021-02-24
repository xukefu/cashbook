package com.xkf.cashbook.service.impl;

import com.xkf.cashbook.common.result.Result;
import com.xkf.cashbook.common.result.ResultGenerator;
import com.xkf.cashbook.mysql.mapper.ConsumeCategoryMapper;
import com.xkf.cashbook.mysql.model.ConsumeCategoryDO;
import com.xkf.cashbook.service.IConsumeCategoryService;
import com.xkf.cashbook.pojo.vo.ConsumeCategoryVO;
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
