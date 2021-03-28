package com.xkf.cashbook.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.xkf.cashbook.common.utils.PeriodsUtil;
import com.xkf.cashbook.common.result.Result;
import com.xkf.cashbook.common.result.ResultGenerator;
import com.xkf.cashbook.common.constant.Period;
import com.xkf.cashbook.pojo.dto.ConsumeProportionDTO;
import com.xkf.cashbook.pojo.dto.FanChartDTO;
import com.xkf.cashbook.pojo.dto.LineChartDTO;
import com.xkf.cashbook.mysql.mapper.ConsumeCategoryMapper;
import com.xkf.cashbook.mysql.mapper.AdminMapper;
import com.xkf.cashbook.service.AdminService;
import com.xkf.cashbook.pojo.vo.ConsumeReportVO;
import com.xkf.cashbook.pojo.vo.TableVO;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * 后台报表
 *
 * @author xukf01
 */
@Service
public class AdminServiceImpl implements AdminService {

    @Resource
    private AdminMapper adminMapper;

    @Resource
    private ConsumeCategoryMapper consumeCategoryMapper;

    @Override
    public TableVO getSumTotalTableByCategory(Long familyId) {
        TableVO tableVO = new TableVO();
        List<ConsumeReportVO> tableByCategory = adminMapper.getSumTotalTableByCategory(familyId);
        if (!CollectionUtils.isEmpty(tableByCategory)) {
            for (ConsumeReportVO consumeReportVO : tableByCategory) {
                String categoryName = consumeCategoryMapper.getCategoryNameById(consumeReportVO.getConsumeCategoryId());
                consumeReportVO.setType(categoryName);
            }
        }
        tableVO.setList(tableByCategory);
        return tableVO;
    }

    @Override
    public Result getMonthlyConsumeAmount(Long familyId) {
        Period period = PeriodsUtil.getPeriodByType(3);
        Double amount = adminMapper.getConsumeAmountByDate(period.getStartDate(), period.getEndDate(),familyId);
        return ResultGenerator.genSuccessResult(null, amount);
    }

    @Override
    public Result getWeeklyConsumeAmount(Long familyId) {
        Period period = PeriodsUtil.getPeriodByType(1);
        Double amount = adminMapper.getConsumeAmountByDate(period.getStartDate(), period.getEndDate(),familyId);
        return ResultGenerator.genSuccessResult(null, amount);
    }

    @Override
    public Result getMonthlyIncomeAmount(Long familyId) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        String consumeDate = year + "-" + String.format("%02d", month) + "-" + "01";
        Double amount = adminMapper.getIncomeAmountByDate(consumeDate,familyId);
        return ResultGenerator.genSuccessResult(null, amount);
    }

    @Override
    public Result getMonthlyConsumeProportion(Long familyId) {
        Period periodByType = PeriodsUtil.getPeriodByType(3);
        List<ConsumeProportionDTO> consumeProportionDTOS = adminMapper.getMonthlyConsumeProportion(periodByType.getStartDate(), periodByType.getEndDate(),familyId);
        //月度总金额
        Double monthlyAmount = adminMapper.getConsumeAmountByDate(periodByType.getStartDate(), periodByType.getEndDate(),familyId);
        Double top5Amount = 0d;
        if (!CollectionUtils.isEmpty(consumeProportionDTOS)) {
            for (ConsumeProportionDTO consumeProportion : consumeProportionDTOS) {
                String categoryName = consumeCategoryMapper.getCategoryNameById(consumeProportion.getConsumeCategoryId());
                consumeProportion.setConsumeCategoryName(categoryName);
                top5Amount = NumberUtil.add(top5Amount, consumeProportion.getAmount());
            }
        }
        ConsumeProportionDTO other = new ConsumeProportionDTO();
        other.setConsumeCategoryName("其他");
        other.setAmount(NumberUtil.sub(monthlyAmount, top5Amount));
        consumeProportionDTOS.add(other);

        List<FanChartDTO> fanCharts = new ArrayList<>(consumeProportionDTOS.size());
        for (ConsumeProportionDTO consumeProportionDTO : consumeProportionDTOS) {
            FanChartDTO fanChartDTO = new FanChartDTO(consumeProportionDTO.getAmount(), consumeProportionDTO.getConsumeCategoryName());
            fanCharts.add(fanChartDTO);
        }

        //计算其他的比例
        return ResultGenerator.genSuccessResult(null, fanCharts);
    }

    @Override
    public Result getEveryDayConsumeAmount(int days, Long familyId) {
        List<LineChartDTO> lineChartDTOS = adminMapper.getEveryDayConsumeAmount(-days,familyId);
        if (lineChartDTOS.isEmpty()) {
            return ResultGenerator.genFailResult();
        }
        //去掉年 前端挤不下...
        for (LineChartDTO lineChartDTO : lineChartDTOS) {
            lineChartDTO.setDate(lineChartDTO.getDate().substring(5, lineChartDTO.getDate().length()));
        }
        return ResultGenerator.genSuccessResult(null, lineChartDTOS);
    }

}
