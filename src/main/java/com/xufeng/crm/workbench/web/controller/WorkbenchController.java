package com.xufeng.crm.workbench.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WorkbenchController {
    @RequestMapping("/workbench/index.do")
    public String workbenchIndex() {
        return "workbench/index";
    }
}

