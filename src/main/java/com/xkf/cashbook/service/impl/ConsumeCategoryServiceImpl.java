package com.xkf.cashbook.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xkf.cashbook.common.result.Result;
import com.xkf.cashbook.common.result.ResultGenerator;
import com.xkf.cashbook.mysql.mapper.ConsumeCategoryMapper;
import com.xkf.cashbook.mysql.mapper.FamilyCategoryMapper;
import com.xkf.cashbook.mysql.model.ConsumeCategoryDO;
import com.xkf.cashbook.mysql.model.FamilyCategoryDO;
import com.xkf.cashbook.pojo.dto.ConsumeCategoryDTO;
import com.xkf.cashbook.service.IConsumeCategoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 消费分类
 *
 * @author xukf01
 */
@Service
public class ConsumeCategoryServiceImpl implements IConsumeCategoryService {

    @Resource
    private ConsumeCategoryMapper consumeCategoryMapper;

    @Resource
    private FamilyCategoryMapper familyCategoryMapper;

    @Override
    public List<ConsumeCategoryDTO> getAll(Long FamilyId) {
        List<ConsumeCategoryDO> consumeCategorys = consumeCategoryMapper.getAll(FamilyId);
        return consumeCategorys.stream().map(
                consumeCategoryDO ->
                        BeanUtil.copyProperties(consumeCategoryDO, ConsumeCategoryDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Result add(String categoryName,Long familyId) {
        QueryWrapper<ConsumeCategoryDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(ConsumeCategoryDO::getCategoryName,categoryName);
        List<ConsumeCategoryDO> consumeCategorys = consumeCategoryMapper.selectList(queryWrapper);
        if (!CollectionUtil.isEmpty(consumeCategorys)){
            return ResultGenerator.genFailResult("分类已经存在了");
        }
        ConsumeCategoryDO consumeCategoryDO = new ConsumeCategoryDO(categoryName);
        int insert = consumeCategoryMapper.insert(consumeCategoryDO);
        if (insert == 1) {
            //记录关联表
            FamilyCategoryDO familyCategoryDO = new FamilyCategoryDO();
            familyCategoryDO
                    .setCategoryId(consumeCategoryDO.getId())
                    .setCategoryType(1)
                    .setFamilyId(familyId);
            familyCategoryMapper.insert(familyCategoryDO);
            return ResultGenerator.genSuccessResult("添加成功");
        }
        return ResultGenerator.genFailResult("添加失败!");
    }
}
