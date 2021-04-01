package com.xkf.cashbook.controller;


import com.xkf.cashbook.common.result.Result;
import com.xkf.cashbook.common.result.ResultGenerator;
import com.xkf.cashbook.service.IncomeCategoryService;
import com.xkf.cashbook.pojo.vo.ConsumeCategoryVO;
import com.xkf.cashbook.pojo.vo.IncomeCategoryVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @author xukf01
 */
@RestController
@RequestMapping("income/category")
public class IncomeCategoryController extends BaseController {
    @Resource
    private IncomeCategoryService incomeCategoryService;

    @Value("${common.length.max}")
    private Integer categoryLength;

    @RequestMapping("getAll")
    public Result getAll() {
        return incomeCategoryService.getAll();
    }

    @RequestMapping("/add")
    public Result add(@RequestParam(name = "categoryName") String categoryName, HttpServletRequest request) {
        Long familyId = getFamilyId(request);
        if (Objects.isNull(familyId)) {
            return ResultGenerator.genFailResult("您还没创建/加入任何家庭哦");
        }
        if (StringUtils.isEmpty(categoryName) && StringUtils.isEmpty(categoryName.trim())) {
            return ResultGenerator.genFailResult("分类名称不能为空");
        }
        if (categoryName.length() > categoryLength) {
            return ResultGenerator.genFailResult("分类名称最多"+categoryLength+"个字哦");
        }
        return incomeCategoryService.add(categoryName, familyId);
    }
}
