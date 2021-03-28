package com.xkf.cashbook.controller;


import com.xkf.cashbook.common.result.Result;
import com.xkf.cashbook.common.result.ResultGenerator;
import com.xkf.cashbook.service.AdminService;
import com.xkf.cashbook.pojo.vo.TableVO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/** 后台报表
 * @author xukf01
 */
@RestController
@RequestMapping("admin")
public class AdminController extends BaseController{

    @Resource
    private AdminService adminService;

    @RequestMapping("getMonthlyConsumeAmount")
    public Result  getMonthlyConsumeAmount(HttpServletRequest request){
        Long familyId = getFamilyId(request);
        if (Objects.isNull(familyId)){
            return ResultGenerator.genFailResult("参数有误,家庭id不能为空");
        }
        return adminService.getMonthlyConsumeAmount(familyId);
    }

    @RequestMapping("getWeeklyConsumeAmount")
    public Result  getWeeklyConsumeAmount(HttpServletRequest request){
        Long familyId = getFamilyId(request);
        if (Objects.isNull(familyId)){
            return ResultGenerator.genFailResult("参数有误,家庭id不能为空");
        }
        return adminService.getWeeklyConsumeAmount(familyId);
    }

    @RequestMapping("getMonthlyIncomeAmount")
    public Result  getMonthlyIncomeAmount(HttpServletRequest request){
        Long familyId = getFamilyId(request);
        if (Objects.isNull(familyId)){
            return ResultGenerator.genFailResult("参数有误,家庭id不能为空");
        }
        return adminService.getMonthlyIncomeAmount(familyId);
    }

    @RequestMapping("getMonthlyConsumeProportion")
    public Result  getMonthlyConsumeProportion(HttpServletRequest request){
        Long familyId = getFamilyId(request);
        if (Objects.isNull(familyId)){
            return ResultGenerator.genFailResult("参数有误,家庭id不能为空");
        }
        return adminService.getMonthlyConsumeProportion(familyId);
    }

    @RequestMapping("getEveryDayConsumeAmount")
    public Result  getEveryDayConsumeAmount(@RequestParam("days") int days,HttpServletRequest request){
        Long familyId = getFamilyId(request);
        if (Objects.isNull(familyId)){
            return ResultGenerator.genFailResult("参数有误,家庭id不能为空");
        }
        return adminService.getEveryDayConsumeAmount(days,familyId);
    }

    @RequestMapping("getTableVO")
    public TableVO get(HttpServletRequest request) {
        Long familyId = getFamilyId(request);
        if (Objects.isNull(familyId)){
//            return ResultGenerator.genFailResult("参数有误,家庭id不能为空");
        }
        return adminService.getSumTotalTableByCategory(familyId);
    }
}
