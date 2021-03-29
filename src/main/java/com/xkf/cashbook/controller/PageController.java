package com.xkf.cashbook.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 前端跳转页面用,不做鉴权
 *
 * @author xukf01
 */
@Controller
public class PageController {

    @RequestMapping("/page/index")
    public String index() {
        return "index";
    }

    @RequestMapping("")
    public String index2() {
        return "index";
    }

    @RequestMapping("/page/admin")
    public String admin() {
        return "admin";
    }

    @RequestMapping("/page/home")
    public String home() {
        return "home";
    }

    @RequestMapping("/page/myFamily")
    public String family() {
        return "family/myFamily";
    }
}
