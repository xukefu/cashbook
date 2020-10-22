package com.xkf.cashbook.web.controller;


import com.xkf.cashbook.common.Result;
import com.xkf.cashbook.web.service.AdminService;
import com.xkf.cashbook.web.vo.TableVO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;

@Controller
public class AdminController {

    @Resource
    private AdminService adminService;

    @GetMapping(value = "/admin")
    public String admin() {
        return "admin";
    }

    @RequestMapping("getMonthlyConsumeAmount")
    @ResponseBody
    public Result  getMonthlyConsumeAmount(){
        return adminService.getMonthlyConsumeAmount();
    }

    @RequestMapping("getWeeklyConsumeAmount")
    @ResponseBody
    public Result  getWeeklyConsumeAmount(){
        return adminService.getWeeklyConsumeAmount();
    }

    @RequestMapping("getMonthlyIncomeAmount")
    @ResponseBody
    public Result  getMonthlyIncomeAmount(){
        return adminService.getMonthlyIncomeAmount();
    }

    @RequestMapping("getMonthlyConsumeProportion")
    @ResponseBody
    public Result  getMonthlyConsumeProportion(){
        return adminService.getMonthlyConsumeProportion();
    }

    @RequestMapping("getEveryDayConsumeAmount")
    @ResponseBody
    public Result  getEveryDayConsumeAmount(@RequestParam("days") int days){
        return adminService.getEveryDayConsumeAmount(days);
    }

    @ResponseBody
    @RequestMapping("getTableVO")
    public TableVO get() {
        return adminService.getSumTotalTableByCategory();
    }
}
