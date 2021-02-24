package com.xkf.cashbook.controller;


import com.xkf.cashbook.common.result.Result;
import com.xkf.cashbook.common.result.ResultGenerator;
import com.xkf.cashbook.service.IncomeCategoryService;
import com.xkf.cashbook.pojo.vo.ConsumeCategoryVO;
import com.xkf.cashbook.pojo.vo.IncomeCategoryVO;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author xukf01
 */
@RestController
@RequestMapping("income/category")
public class IncomeCategoryController {
    @Resource
    private IncomeCategoryService incomeCategoryService;

    @RequestMapping("getAll")
    public Result getAll() {
        return incomeCategoryService.getAll();
    }

    @RequestMapping("/add")
    public Result add(@RequestParam(name = "categoryName") String categoryName) {
        if (StringUtils.isEmpty(categoryName) && StringUtils.isEmpty(categoryName.trim())) {
            return ResultGenerator.genFailResult("分类名称不能为空!");
        }
        return incomeCategoryService.add(categoryName);
    }
}
