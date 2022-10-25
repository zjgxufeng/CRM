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
import java.util.*;

import static javafx.scene.input.KeyCode.C;
import static javafx.scene.input.KeyCode.R;

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
    public @ResponseBody
    Object savecreateActivity(Activity activity, HttpSession session) {
        User user = (User) session.getAttribute(Contants.SESSION_USER);

        //封装参数
        activity.setId(UUIDUtils.getUUID());
        activity.setCreateTime(DateUtils.formateDate(new Date()));
        activity.setCreateBy(user.getId());

        ReturnObject returnObject = new ReturnObject();
        //对数据库进行增删改,可能出现问题都需要trycatch
        try {
            //保存市场活动
            int count = activityService.saveCreateActivity(activity);
            if (count <= 0) {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage(Contants.SYSTEM_BUSY);
            } else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage(Contants.SYSTEM_BUSY);
        }
        return returnObject;
    }

    @RequestMapping("/workbench/activity/queryActivityByConditionForPage.do")
    public @ResponseBody
    Object queryActivityByConditionForPage(String owner, String name, String startDate,
                                           String endDate, int pageNo, int pageSize) {
        //封装参数
        Map<String, Object> map = new HashMap<>();
        map.put("owner", owner);
        map.put("name", name);
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        //beginNo是当前页的第一条记录坐标
        map.put("beginNo", (pageNo - 1) * pageSize);
        map.put("pageSize", pageSize);
        //模糊查询
        List<Activity> activityList = activityService.queryActivityByConditionForPage(map);
        //查询所有市场活动的数量
        int totalRows = activityService.queryCountOfActivityByCondition(map);
        //返回所有活动和总记录条数
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("activityList", activityList);
        returnMap.put("totalRows", totalRows);
        return returnMap;
    }

    @RequestMapping("workbench/activity/deleteActivityByIds.do")
    public @ResponseBody
    Object deleteActivityByIds(String[] id) {

        ReturnObject returnObject = new ReturnObject();
        try {
            int returnInt = activityService.deleteActivityByIds(id);
            if (returnInt > 0) {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            } else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage(Contants.SYSTEM_BUSY);
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage(Contants.SYSTEM_BUSY);
        }
        return returnObject;
    }

    @RequestMapping("/workbench/activity/queryActivityById.do")
    public @ResponseBody
    Object queryActivityById(String id) {
        Activity activity = activityService.queryActivityById(id);
        return activity;
    }

    @RequestMapping("/workbench/activity/updateActivityById.do")
  public @ResponseBody Object updateActivityById(Activity activity,HttpSession session){
           User user = (User) session.getAttribute(Contants.SESSION_USER);
           activity.setCreateBy(user.getId());
           activity.setCreateTime(DateUtils.formateDateTime(new Date()));
           ReturnObject returnObject = new ReturnObject();
        try {
            int returnInt = activityService.updateActivityById(activity);
            if (returnInt>0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage(Contants.SYSTEM_BUSY);
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage(Contants.SYSTEM_BUSY);
        }
        return returnObject;
    }
}
