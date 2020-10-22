package com.xkf.cashbook.web.controller;

import com.xkf.cashbook.common.Result;
import com.xkf.cashbook.common.ResultGenerator;
import com.xkf.cashbook.web.service.IConsumeCategoryService;
import com.xkf.cashbook.web.vo.ConsumeCategoryVO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

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
}
