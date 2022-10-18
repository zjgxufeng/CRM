package com.xufeng.crm.settings.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserController {
    @RequestMapping("/settings/qx/user/toLogin.do")
    public String toLogin() {
        //跳转到页面
        return "settings/qx/user/login";
    }
}
