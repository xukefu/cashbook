package com.xkf.cashbook.controller;

import com.xkf.cashbook.common.result.Result;
import com.xkf.cashbook.common.result.ResultGenerator;
import com.xkf.cashbook.service.IConsumeCategoryService;
import com.xkf.cashbook.pojo.vo.ConsumeCategoryVO;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author xukf01
 */
@RestController
@RequestMapping("consume/category")
public class ConsumeCategoryController {

    @Resource
    private IConsumeCategoryService consumeCategoryService;

    @RequestMapping("/getAll")
    public Result getAll() {
        List<ConsumeCategoryVO> consumeCategorys = consumeCategoryService.getAll();
        return ResultGenerator.genSuccessResult(consumeCategorys);
    }

    @RequestMapping("/add")
    public Result add(@RequestParam(name = "categoryName") String categoryName) {
        if (StringUtils.isEmpty(categoryName) && StringUtils.isEmpty(categoryName.trim())) {
            return ResultGenerator.genFailResult("分类名称不能为空!");
        }
        return consumeCategoryService.add(categoryName);
    }
}
