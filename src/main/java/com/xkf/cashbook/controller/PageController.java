package com.xkf.cashbook.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 前端跳转页面用,不做鉴权
 *
 * @author xukf01
 */
@Controller
@RequestMapping("/page")
public class PageController {

    @RequestMapping("/index")
    public String index() {
        return "index";
    }

    @RequestMapping("/admin")
    public String admin() {
        return "admin";
    }

    @RequestMapping("/home")
    public String home() {
        return "home/index";
    }

    @RequestMapping("/myFamily")
    public String family() {
        return "family/myFamily";
    }

    @RequestMapping("/email")
    public String familyApply() {
        return "email/index";
    }
}
