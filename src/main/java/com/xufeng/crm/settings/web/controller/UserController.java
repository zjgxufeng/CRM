package com.xufeng.crm.settings.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {
    @RequestMapping("/settings/qx/user/toLogin.do")
    public String toLogin() {
        //跳转到页面
        return "settings/qx/user/login";
    }

    @RequestMapping("/workbench/index.do")
    public Object Login(String loginAct,String loginPwd,String isRemPwd){
        Map<String,Object> map = new HashMap<>();
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);

        return "workbench/index";
    }
}
