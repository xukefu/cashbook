package com.xkf.cashbook.controller;

import cn.hutool.core.bean.BeanUtil;
import com.xkf.cashbook.common.result.Result;
import com.xkf.cashbook.common.result.ResultGenerator;
import com.xkf.cashbook.mysql.model.ConsumeDetailDO;
import com.xkf.cashbook.mysql.model.IncomeDetailDO;
import com.xkf.cashbook.pojo.dto.IncomeDetailDTO;
import com.xkf.cashbook.service.IIncomeDetailService;
import com.xkf.cashbook.pojo.vo.ConsumeDetailPageVO;
import com.xkf.cashbook.pojo.vo.ConsumeDetailVO;
import com.xkf.cashbook.pojo.vo.IncomeDetailPageVO;
import com.xkf.cashbook.pojo.vo.IncomeDetailVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
@Slf4j
public class IncomeDetailController extends BaseController {

    @Resource
    private IIncomeDetailService incomeDetailService;

    @Value("${comments.length.max}")
    private Integer commentsLength;

    @PostMapping("saveOrUpdate")
    @ResponseBody
    public Result saveOrUpdate(@RequestBody IncomeDetailVO incomeDetailVO, HttpServletRequest request) {
        if (Objects.isNull(incomeDetailVO.getIncomeCategoryId()) || incomeDetailVO.getIncomeCategoryId() < 1) {
            return ResultGenerator.genFailResult("收入类别不能为空!");
        }
        if (incomeDetailVO.getIncomeAmount() == null) {
            return ResultGenerator.genFailResult("收入金额不能为空!");
        }
        if (Objects.isNull(incomeDetailVO.getIncomeDate())) {
            return ResultGenerator.genFailResult("收入时间不能为空");
        }
        if (!Objects.isNull(incomeDetailVO.getIncomeComment()) && incomeDetailVO.getIncomeComment().length() > commentsLength) {
            return ResultGenerator.genFailResult("收入备注最多"+commentsLength+"个字哦");
        }
        Long userId = getUserId(request);
        if (Objects.isNull(userId)) {
            return ResultGenerator.genFailResult("用户信息异常,ID不能为空");
        }
        //只能修改自己的记录
        if (!Objects.isNull(incomeDetailVO.getId())) {
            IncomeDetailDO incomeDetailDO = incomeDetailService.getById(incomeDetailVO.getId());
            if (!userId.equals(incomeDetailDO.getIncomeBy())) {
                log.warn("用户:{}尝试修改其他用户:{}的记录", userId, incomeDetailDO.getIncomeBy());
                return ResultGenerator.genFailResult("不能修改其他用户的记录哦");
            }
        }
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
    public Result pageDetail(@RequestBody IncomeDetailPageVO incomeDetailPageVO, HttpServletRequest request) {
        Long familyId = getFamilyId(request);
        if (Objects.isNull(familyId)) {
            return ResultGenerator.genFailResult("您还没创建/加入任何家庭哦");
        }
        return incomeDetailService.pageDetail(incomeDetailPageVO, familyId);
    }
}
