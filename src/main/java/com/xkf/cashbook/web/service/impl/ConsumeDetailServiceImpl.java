package com.xkf.cashbook.web.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xkf.cashbook.common.PeriodsUtil;
import com.xkf.cashbook.common.Result;
import com.xkf.cashbook.common.ResultGenerator;
import com.xkf.cashbook.common.constant.Period;
import com.xkf.cashbook.web.dto.ConsumeDetailDTO;
import com.xkf.cashbook.web.dto.ConsumeDetailPageDTO;
import com.xkf.cashbook.web.mapper.ConsumeCategoryMapper;
import com.xkf.cashbook.web.mapper.ConsumeDetailMapper;
import com.xkf.cashbook.web.mysql.ConsumeDetailDO;
import com.xkf.cashbook.web.service.IConsumeDetailService;
import com.xkf.cashbook.web.vo.ConsumeDetailPageVO;
import com.xkf.cashbook.web.vo.ConsumeDetailVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ConsumeDetailServiceImpl extends ServiceImpl<ConsumeDetailMapper,ConsumeDetailDO> implements IConsumeDetailService {

    @Resource
    private ConsumeDetailMapper consumeDetailMapper;

    @Resource
    private ConsumeCategoryMapper consumeCategoryMapper;

    @Override
    public Result getLastDetail(int size) {
        List<ConsumeDetailVO> consumeDetails = consumeDetailMapper.getLastDetail(size);
        if (!CollectionUtils.isEmpty(consumeDetails)) {
            for (ConsumeDetailVO consumeDetailVO : consumeDetails) {
                String categoryName = consumeCategoryMapper.getCategoryNameById(consumeDetailVO.getConsumeCategoryId());
                consumeDetailVO.setConsumeCategoryName(categoryName);
            }
        }
        return ResultGenerator.genSuccessResult(consumeDetails);
    }

    @Override
    public ConsumeDetailPageDTO pageDetail(ConsumeDetailPageVO consumeDetailPageVO) {
        QueryWrapper<ConsumeDetailDO> wrapper = new QueryWrapper<>();
        //时间倒序
        wrapper.lambda().orderByDesc(ConsumeDetailDO::getConsumeDate);
        wrapper.lambda().orderByDesc(ConsumeDetailDO::getId);
        //获取查询时间段
        Period period = PeriodsUtil.getPeriodByType(consumeDetailPageVO.getConsumeDateType());
        if (!Objects.isNull(period)) {
            consumeDetailPageVO.setConsumeStartDate(period.getStartDate());
            consumeDetailPageVO.setConsumeEndDate(period.getEndDate());
        }
        //查询条件
        if (!StringUtils.isEmpty(consumeDetailPageVO.getConsumeBy())) {
            wrapper.lambda().eq(ConsumeDetailDO::getConsumeBy, consumeDetailPageVO.getConsumeBy());
        }
        if (!StringUtils.isEmpty(consumeDetailPageVO.getConsumeWay())) {
            wrapper.lambda().eq(ConsumeDetailDO::getConsumeWay, consumeDetailPageVO.getConsumeWay());
        }
        if (consumeDetailPageVO.getConsumeCategoryId() != null) {
            wrapper.lambda().eq(ConsumeDetailDO::getConsumeCategoryId, consumeDetailPageVO.getConsumeCategoryId());
        }
        if (consumeDetailPageVO.getConsumeStartDate() != null) {
            wrapper.lambda().ge(ConsumeDetailDO::getConsumeDate, consumeDetailPageVO.getConsumeStartDate());
        }
        if (consumeDetailPageVO.getConsumeEndDate() != null) {
            wrapper.lambda().le(ConsumeDetailDO::getConsumeDate, consumeDetailPageVO.getConsumeEndDate());
        }
        //条件下的总金额
        Double totalConsumeAmount = consumeDetailMapper.selectTotalConsumeAmount(wrapper);
        Page<ConsumeDetailDO> page = new Page<>(consumeDetailPageVO.getCurrentPage(), consumeDetailPageVO.getPageSize());
        Page<ConsumeDetailDO> consumeDetailPage = consumeDetailMapper.selectPage(page, wrapper);


        Double subtotalConsumeAmount = consumeDetailPage.getRecords().stream().mapToDouble(ConsumeDetailDO::getConsumeAmount).sum();
        BigDecimal subtotalConsumeAmountDecimal = new BigDecimal(subtotalConsumeAmount.toString()).setScale(2, BigDecimal.ROUND_HALF_UP);

        List<ConsumeDetailDTO> consumeDetails = consumeDetailPage.getRecords().stream()
                .map(consumeDetailDO -> BeanUtil.copyProperties(consumeDetailDO, ConsumeDetailDTO.class))
                .collect(Collectors.toList());
        setCategoryName(consumeDetails);

        ConsumeDetailDTO subTotalConsumeDetailDTO = new ConsumeDetailDTO();
        subTotalConsumeDetailDTO.setConsumeBy("小计");
        subTotalConsumeDetailDTO.setConsumeAmount(subtotalConsumeAmountDecimal.doubleValue());
        subTotalConsumeDetailDTO.setConsumeCategoryName("-");

        ConsumeDetailDTO totalConsumeDetailDTO = new ConsumeDetailDTO();
        totalConsumeDetailDTO.setConsumeBy("总计");
        totalConsumeDetailDTO.setConsumeAmount(totalConsumeAmount);
        totalConsumeDetailDTO.setConsumeCategoryName("-");

        consumeDetails.add(subTotalConsumeDetailDTO);
        consumeDetails.add(totalConsumeDetailDTO);

        return new ConsumeDetailPageDTO(consumeDetailPage.getTotal(), consumeDetails);
    }

    private void setCategoryName(List<ConsumeDetailDTO> consumeDetails) {
        if (!CollectionUtils.isEmpty(consumeDetails)) {
            for (ConsumeDetailDTO consumeDetailDTO : consumeDetails) {
                String categoryName = consumeCategoryMapper.getCategoryNameById(consumeDetailDTO.getConsumeCategoryId());
                consumeDetailDTO.setConsumeCategoryName(categoryName);
            }
        }
    }
}
