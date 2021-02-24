package com.xkf.cashbook.web.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xkf.cashbook.common.Result;
import com.xkf.cashbook.common.ResultGenerator;
import com.xkf.cashbook.web.dto.IncomeDetailDTO;
import com.xkf.cashbook.web.dto.IncomeDetailPageDTO;
import com.xkf.cashbook.web.mapper.IncomeCategoryMapper;
import com.xkf.cashbook.web.mapper.IncomeDetailMapper;
import com.xkf.cashbook.web.mysql.IncomeCategoryDO;
import com.xkf.cashbook.web.mysql.IncomeDetailDO;
import com.xkf.cashbook.web.service.IIncomeDetailService;
import com.xkf.cashbook.web.vo.IncomeDetailPageVO;
import com.xkf.cashbook.web.vo.IncomeDetailVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author xukf01
 */
@Service
public class IncomeDetailServiceImpl extends ServiceImpl<IncomeDetailMapper,IncomeDetailDO> implements IIncomeDetailService {
    @Resource
    private IncomeDetailMapper incomeDetailMapper;

    @Resource
    private IncomeCategoryMapper incomeCategoryMapper;

    @Override
    @Transactional
    public Result add(IncomeDetailVO incomeDetailVO) {
        incomeDetailVO.setRecordBy(incomeDetailVO.getIncomeBy());
        incomeDetailVO.setRecordDate(LocalDateTime.now());
        IncomeDetailDO incomeDetailDO = BeanUtil.copyProperties(incomeDetailVO, IncomeDetailDO.class);
        int add = incomeDetailMapper.insert(incomeDetailDO);
        if (add == 1) {
            return ResultGenerator.genSuccessResult("记录成功!", null);
        }
        return ResultGenerator.genFailResult();
    }

    @Override
    public IncomeDetailPageDTO pageDetail(IncomeDetailPageVO incomeDetailPageVO) {
        QueryWrapper<IncomeDetailDO> wrapper = new QueryWrapper<>();
        //时间倒序
        wrapper.lambda().orderByDesc(IncomeDetailDO::getIncomeDate);
        wrapper.lambda().orderByDesc(IncomeDetailDO::getId);

        Page<IncomeDetailDO> page = new Page<>(incomeDetailPageVO.getCurrentPage(), incomeDetailPageVO.getPageSize());
        Page<IncomeDetailDO> incomeDetailPage = incomeDetailMapper.selectPage(page, wrapper);
        if (incomeDetailPage.getTotal() == 0){
            return new IncomeDetailPageDTO();
        }
        List<IncomeDetailDTO> incomeDetails = incomeDetailPage.getRecords().stream()
                .map(incomeDetailDO ->
                        BeanUtil.copyProperties(incomeDetailDO, IncomeDetailDTO.class))
                .collect(Collectors.toList());
        setCategoryName(incomeDetails);
        return new IncomeDetailPageDTO(incomeDetailPage.getTotal(), incomeDetails);
    }

    private void setCategoryName(List<IncomeDetailDTO> incomeDetails) {
        if (!CollectionUtils.isEmpty(incomeDetails)) {
            List<Long> categoryIds = incomeDetails.stream().map(IncomeDetailDTO::getIncomeCategoryId)
                    .collect(Collectors.toList());
            QueryWrapper<IncomeCategoryDO> wrapper = new QueryWrapper<>();
            wrapper.lambda().in(IncomeCategoryDO::getId,categoryIds);
            List<IncomeCategoryDO> incomeCategoryDos = incomeCategoryMapper.selectList(wrapper);
            Map<Long, List<IncomeCategoryDO>> categoryMap = incomeCategoryDos.stream().collect(Collectors.groupingBy(IncomeCategoryDO::getId));
            incomeDetails.forEach(incomeDetailDTO -> incomeDetailDTO.setIncomeCategoryName(categoryMap.get(incomeDetailDTO.getIncomeCategoryId()).get(0).getCategoryName()));
        }
    }
}
