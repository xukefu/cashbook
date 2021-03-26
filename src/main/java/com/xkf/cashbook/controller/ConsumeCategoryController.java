package com.xkf.cashbook.controller;

import com.xkf.cashbook.common.result.Result;
import com.xkf.cashbook.common.result.ResultGenerator;
import com.xkf.cashbook.mysql.model.ConsumeCategoryDO;
import com.xkf.cashbook.pojo.dto.ConsumeCategoryDTO;
import com.xkf.cashbook.pojo.dto.FamilyDTO;
import com.xkf.cashbook.service.IConsumeCategoryService;
import com.xkf.cashbook.pojo.vo.ConsumeCategoryVO;
import com.xkf.cashbook.service.IFamilyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

/**
 * @author xukf01
 */
@RestController
@RequestMapping("consume/category")
@Slf4j
public class ConsumeCategoryController extends BaseController {

    @Resource
    private IConsumeCategoryService consumeCategoryService;

    @Resource
    private IFamilyService familyService;

    @RequestMapping("/getAll")
    public Result getAll(HttpServletRequest request) {
        Long familyId = getFamilyId(request);
        if (Objects.isNull(familyId)){
            return ResultGenerator.genFailResult("您还未加入任何家庭,无法记账哦");
        }
        List<ConsumeCategoryDTO> consumeCategorys = consumeCategoryService.getAll(familyId);
        return ResultGenerator.genSuccessResult(consumeCategorys);
    }

    @RequestMapping("/add")
    public Result add(@RequestParam(name = "categoryName") String categoryName,HttpServletRequest request) {
        if (StringUtils.isEmpty(categoryName) && StringUtils.isEmpty(categoryName.trim())) {
            return ResultGenerator.genFailResult("分类名称不能为空!");
        }
        Long familyId = getFamilyId(request);
        if (Objects.isNull(familyId)){
            return ResultGenerator.genFailResult("您还未加入任何家庭,无法记账哦");
        }
        return consumeCategoryService.add(categoryName,familyId);
    }


}
