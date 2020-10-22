package com.xkf.cashbook.web.controller;

import com.xkf.cashbook.common.Result;
import com.xkf.cashbook.web.service.IIncomeDetailService;
import com.xkf.cashbook.web.vo.IncomeDetailVO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("income/detail")
public class IncomeDetailController {

    @Resource
    private IIncomeDetailService incomeDetailService;

    @RequestMapping("/add")
    @ResponseBody
    public Result add(@RequestBody IncomeDetailVO incomeDetailVO) {
        return incomeDetailService.add(incomeDetailVO);
    }
}
