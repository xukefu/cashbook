package com.xkf.cashbook.web.service.impl;

import com.xkf.cashbook.web.mapper.ConsumeCategoryMapper;
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
}
