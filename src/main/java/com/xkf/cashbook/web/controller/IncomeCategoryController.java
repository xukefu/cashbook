package com.xkf.cashbook.web.controller;


import com.xkf.cashbook.common.Result;
import com.xkf.cashbook.web.service.IncomeCategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("income/category")
public class IncomeCategoryController {
    @Resource
    private IncomeCategoryService incomeCategoryService;

    @RequestMapping("getAll")
    @ResponseBody
    public Result getAll() {
        return incomeCategoryService.getAll();
    }
}
