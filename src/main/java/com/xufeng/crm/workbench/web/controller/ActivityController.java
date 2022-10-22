package com.xufeng.crm.workbench.web.controller;

import com.xufeng.crm.settings.common.domain.ReturnObject;
import com.xufeng.crm.settings.common.util.DateUtils;
import com.xufeng.crm.settings.common.util.UUIDUtils;
import com.xufeng.crm.settings.contants.Contants;
import com.xufeng.crm.settings.domain.User;
import com.xufeng.crm.settings.service.UserService;
import com.xufeng.crm.workbench.domain.Activity;
import com.xufeng.crm.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
public class ActivityController {
    @Autowired
    private UserService userService;
    @Autowired
    private ActivityService activityService;

    @RequestMapping("/workbench/activity/index.do")
    public String index(HttpServletRequest request) {
        //1.查询所有用户
        List<User> userList = userService.queryAllUsers();
        //2.把所有用户保存到请求对象
        request.setAttribute("userList", userList);

        return "workbench/activity/index";
    }

    @RequestMapping("/workbench/activity/savecreateActivity.do")
    //自动封装参数
    public @ResponseBody Object savecreateActivity(Activity activity, HttpSession session) {
        User user = (User) session.getAttribute(Contants.SESSION_USER);

        //封装参数
        String id = UUIDUtils.getUUID();
        activity.setCreateTime(DateUtils.formateDate(new Date()));
        activity.setCreateBy(user.getId());

        ReturnObject returnObject = new ReturnObject();
        //对数据库进行增删改,可能出现问题都需要trycatch
        try {
            //保存市场活动
            int count = activityService.saveCreateActivity(activity);
            if(count<=0){
               returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
               returnObject.setMessage(Contants.SYSTEM_BUSY);
            }else{
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage(Contants.SYSTEM_BUSY);
        }
        return returnObject;
    }

}
