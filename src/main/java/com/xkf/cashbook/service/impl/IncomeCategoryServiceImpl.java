package com.xkf.cashbook.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xkf.cashbook.common.constant.FamilyCategoryType;
import com.xkf.cashbook.common.result.Result;
import com.xkf.cashbook.common.result.ResultGenerator;
import com.xkf.cashbook.mysql.mapper.FamilyCategoryMapper;
import com.xkf.cashbook.mysql.mapper.IncomeCategoryMapper;
import com.xkf.cashbook.mysql.model.ConsumeCategoryDO;
import com.xkf.cashbook.mysql.model.FamilyCategoryDO;
import com.xkf.cashbook.mysql.model.IncomeCategoryDO;
import com.xkf.cashbook.service.IncomeCategoryService;
import com.xkf.cashbook.pojo.vo.IncomeCategoryVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Service
public class IncomeCategoryServiceImpl implements IncomeCategoryService {
    @Resource
    private IncomeCategoryMapper incomeCategoryMapper;

    @Resource
    private FamilyCategoryMapper familyCategoryMapper;

    @Override
    public Result getAll() {
        List<IncomeCategoryVO> incomeCategorys = incomeCategoryMapper.getAll();
        return ResultGenerator.genSuccessResult(incomeCategorys);
    }

    @Override
    @Transactional
    public Result add(String categoryName, Long familyId) {
        QueryWrapper<IncomeCategoryDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(IncomeCategoryDO::getCategoryName, categoryName);
        List<IncomeCategoryDO> incomeCategorys = incomeCategoryMapper.selectList(queryWrapper);
        Long categoryId = null;
        if (!CollectionUtil.isEmpty(incomeCategorys)) {
            QueryWrapper<FamilyCategoryDO> familyCategoryQueryWrapper = new QueryWrapper<>();
            familyCategoryQueryWrapper.lambda()
                    .eq(FamilyCategoryDO::getCategoryId, incomeCategorys.get(0).getId())
                    .eq(FamilyCategoryDO::getFamilyId, familyId)
                    .eq(FamilyCategoryDO::getCategoryType, FamilyCategoryType.INCOME.getType());
            FamilyCategoryDO familyCategoryDO = familyCategoryMapper.selectOne(familyCategoryQueryWrapper);
            if (!Objects.isNull(familyCategoryDO)) {
                return ResultGenerator.genFailResult("分类已经存在了");
            }
            categoryId = incomeCategorys.get(0).getId();
        } else {
            IncomeCategoryDO incomeCategoryDO = new IncomeCategoryDO(categoryName);
            incomeCategoryMapper.insert(incomeCategoryDO);
            categoryId = incomeCategoryDO.getId();
        }
        //记录关联表
        FamilyCategoryDO familyCategoryDO = new FamilyCategoryDO();
        familyCategoryDO
                .setCategoryId(categoryId)
                .setCategoryType(FamilyCategoryType.INCOME.getType())
                .setFamilyId(familyId);
        familyCategoryMapper.insert(familyCategoryDO);
        return ResultGenerator.genSuccessResult("添加成功");
    }
}
