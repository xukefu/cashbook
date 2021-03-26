package com.xkf.cashbook.controller;

import cn.hutool.core.bean.BeanUtil;
import com.xkf.cashbook.common.result.Result;
import com.xkf.cashbook.common.result.ResultGenerator;
import com.xkf.cashbook.mysql.model.IncomeDetailDO;
import com.xkf.cashbook.service.IIncomeDetailService;
import com.xkf.cashbook.pojo.vo.ConsumeDetailPageVO;
import com.xkf.cashbook.pojo.vo.ConsumeDetailVO;
import com.xkf.cashbook.pojo.vo.IncomeDetailPageVO;
import com.xkf.cashbook.pojo.vo.IncomeDetailVO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author xukf01
 */
@Controller
@RequestMapping("income/detail")
public class IncomeDetailController extends BaseController{

    @Resource
    private IIncomeDetailService incomeDetailService;

    @PostMapping("saveOrUpdate")
    @ResponseBody
    public Result saveOrUpdate(@RequestBody IncomeDetailVO incomeDetailVO, HttpServletRequest request) {
        if (Objects.isNull(incomeDetailVO.getIncomeCategoryId()) || incomeDetailVO.getIncomeCategoryId() < 1) {
            return ResultGenerator.genFailResult("收入类别不能为空!");
        }
        if (incomeDetailVO.getIncomeAmount() == null) {
            return ResultGenerator.genFailResult("金额不能为空!");
        }
        Long userId = getUserId(request);
        IncomeDetailDO incomeDetailDO = BeanUtil.copyProperties(incomeDetailVO, IncomeDetailDO.class);
        incomeDetailDO.setRecordDate(LocalDateTime.now());
        incomeDetailDO.setRecordBy(userId);
        incomeDetailDO.setUpdateTime(LocalDateTime.now());
        incomeDetailDO.setIncomeBy(userId);
        boolean result = incomeDetailService.saveOrUpdate(incomeDetailDO);
        if (result) {
            return ResultGenerator.genSuccessResult();
        }
        return ResultGenerator.genFailResult();
    }

    @PostMapping("pageDetail")
    @ResponseBody
    public Result pageDetail(@RequestBody IncomeDetailPageVO incomeDetailPageVO) {
        return ResultGenerator.genSuccessResult(incomeDetailService.pageDetail(incomeDetailPageVO));
    }
}
