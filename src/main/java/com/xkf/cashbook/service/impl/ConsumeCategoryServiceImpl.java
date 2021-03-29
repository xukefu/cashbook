package com.xkf.cashbook.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xkf.cashbook.common.constant.FamilyCategoryType;
import com.xkf.cashbook.common.result.Result;
import com.xkf.cashbook.common.result.ResultGenerator;
import com.xkf.cashbook.mysql.mapper.ConsumeCategoryMapper;
import com.xkf.cashbook.mysql.mapper.FamilyCategoryMapper;
import com.xkf.cashbook.mysql.model.ConsumeCategoryDO;
import com.xkf.cashbook.mysql.model.FamilyCategoryDO;
import com.xkf.cashbook.pojo.dto.ConsumeCategoryDTO;
import com.xkf.cashbook.service.IConsumeCategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
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
    @Transactional
    public Result add(String categoryName, Long familyId) {
        QueryWrapper<ConsumeCategoryDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(ConsumeCategoryDO::getCategoryName, categoryName);
        List<ConsumeCategoryDO> consumeCategorys = consumeCategoryMapper.selectList(queryWrapper);
        Long categoryId = null;
        if (!CollectionUtil.isEmpty(consumeCategorys)) {
            QueryWrapper<FamilyCategoryDO> familyCategoryQueryWrapper = new QueryWrapper<>();
            familyCategoryQueryWrapper.lambda()
                    .eq(FamilyCategoryDO::getCategoryId, consumeCategorys.get(0).getId())
                    .eq(FamilyCategoryDO::getFamilyId, familyId)
                    .eq(FamilyCategoryDO::getCategoryType, FamilyCategoryType.CONSUME.getType());
            FamilyCategoryDO familyCategoryDO = familyCategoryMapper.selectOne(familyCategoryQueryWrapper);
            if (!Objects.isNull(familyCategoryDO)) {
                return ResultGenerator.genFailResult("分类已经存在了");
            }
            categoryId = consumeCategorys.get(0).getId();
        } else {
            ConsumeCategoryDO consumeCategoryDO = new ConsumeCategoryDO(categoryName);
            consumeCategoryMapper.insert(consumeCategoryDO);
            categoryId = consumeCategoryDO.getId();
        }
        //关联表
        FamilyCategoryDO familyCategoryDO = new FamilyCategoryDO();
        familyCategoryDO.setFamilyId(familyId)
                .setCategoryType(FamilyCategoryType.CONSUME.getType())
                .setCategoryId(categoryId);
        familyCategoryMapper.insert(familyCategoryDO);
        return ResultGenerator.genSuccessResult("添加成功");
    }
}
