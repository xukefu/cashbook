package com.xkf.cashbook.controller;

import cn.hutool.core.bean.BeanUtil;
import com.xkf.cashbook.common.result.Result;
import com.xkf.cashbook.common.result.ResultGenerator;
import com.xkf.cashbook.mysql.model.ConsumeDetailDO;
import com.xkf.cashbook.service.IConsumeDetailService;
import com.xkf.cashbook.pojo.vo.ConsumeDetailPageVO;
import com.xkf.cashbook.pojo.vo.ConsumeDetailVO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author xukf01
 */
@RequestMapping("consume/detail")
@Controller
public class ConsumeDetailController extends BaseController{

    @Resource
    private IConsumeDetailService consumeDetailService;

    @PostMapping("saveOrUpdate")
    @ResponseBody
    public Result saveOrUpdate(@RequestBody ConsumeDetailVO consumeDetailVO, HttpServletRequest request) {
        if (Objects.isNull(consumeDetailVO.getConsumeCategoryId()) || consumeDetailVO.getConsumeCategoryId() < 1) {
            return ResultGenerator.genFailResult("消费类别不能为空!");
        }
        if (consumeDetailVO.getConsumeAmount() == null) {
            return ResultGenerator.genFailResult("金额不能为空!");
        }
        Long userId = getUserId(request);
        ConsumeDetailDO consumeDetailDO = BeanUtil.copyProperties(consumeDetailVO, ConsumeDetailDO.class);
        consumeDetailDO.setRecordDate(LocalDateTime.now());
        consumeDetailDO.setRecordBy(userId);
        consumeDetailDO.setUpdateTime(LocalDateTime.now());
        consumeDetailDO.setConsumeBy(userId);
        boolean result = consumeDetailService.saveOrUpdate(consumeDetailDO);
        if (result) {
            return ResultGenerator.genSuccessResult("操作成功!",null);
        }
        return ResultGenerator.genFailResult("操作失败!");
    }

    @GetMapping("getLastDetail")
    @ResponseBody
    public Result getLastDetail(@RequestParam(value = "size") int size) {
        return consumeDetailService.getLastDetail(size);
    }

    @PostMapping("pageDetail")
    @ResponseBody
    public Result pageDetail(@RequestBody ConsumeDetailPageVO consumeDetailPageVO) {
        return ResultGenerator.genSuccessResult(consumeDetailService.pageDetail(consumeDetailPageVO));
    }

}
