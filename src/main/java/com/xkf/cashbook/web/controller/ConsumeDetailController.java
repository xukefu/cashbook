package com.xkf.cashbook.web.controller;

import com.xkf.cashbook.common.Result;
import com.xkf.cashbook.common.ResultGenerator;
import com.xkf.cashbook.web.service.IConsumeDetailService;
import com.xkf.cashbook.web.vo.ConsumeDetailPageVO;
import com.xkf.cashbook.web.vo.ConsumeDetailVO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RequestMapping("consume/detail")
@Controller
public class ConsumeDetailController {

    @Resource
    private IConsumeDetailService consumeDetailService;

    @PostMapping("add")
    @ResponseBody
    public Result add(@RequestBody ConsumeDetailVO consumeDetailVO) {
        if (consumeDetailVO.getConsumeCategoryId() < 1) {
            return ResultGenerator.genFailResult("消费类别不能为空!");
        }
        if (consumeDetailVO.getConsumeAmount() == null){
            return ResultGenerator.genFailResult("金额不能为空!");
        }
        return consumeDetailService.addConsumeDetail(consumeDetailVO);
    }

    @GetMapping("getLastDetail")
    @ResponseBody
    public Result getLastDetail(@RequestParam(value = "size") int size) {
        return consumeDetailService.getLastDetail(size);
    }

    @PostMapping("pageDetail")
    @ResponseBody
    public Result pageDetail(@RequestBody ConsumeDetailPageVO consumeDetailPageVO){
        return ResultGenerator.genSuccessResult(consumeDetailService.pageDetail(consumeDetailPageVO));
    }
}
