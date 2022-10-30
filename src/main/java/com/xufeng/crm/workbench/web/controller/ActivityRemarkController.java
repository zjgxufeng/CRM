package com.xufeng.crm.workbench.web.controller;

import com.xufeng.crm.settings.common.domain.ReturnObject;
import com.xufeng.crm.settings.common.util.DateUtils;
import com.xufeng.crm.settings.common.util.UUIDUtils;
import com.xufeng.crm.settings.contants.Contants;
import com.xufeng.crm.settings.domain.User;
import com.xufeng.crm.workbench.domain.ActivityRemark;
import com.xufeng.crm.workbench.service.ActivityRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
public class ActivityRemarkController {

    @Autowired
    private ActivityRemarkService activityRemarkService;
    @RequestMapping("/workbench/activity/saveCreateActivityRemark.do")
    public @ResponseBody Object saveCreateActivityRemark(ActivityRemark activityRemark, HttpSession session){
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        activityRemark.setId(UUIDUtils.getUUID());
        activityRemark.setCreateTime(DateUtils.formateDateTime(new Date()));
        activityRemark.setCreateBy(user.getId());
        activityRemark.setEditFlag("0");
        int returnInt = activityRemarkService.saveCreateActivityRemark(activityRemark);
        ReturnObject returnObject = new ReturnObject();
        if(returnInt>0){
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            returnObject.setRetData(activityRemark);
        }else{
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage(Contants.SYSTEM_BUSY);
        }
        return returnObject;
    }
    @RequestMapping("/workbench/activity/deleteActivityRemarkById.do")
    public @ResponseBody Object deleteActivityRemarkById(String remarkId){
        int returnInt =  activityRemarkService.deleteActivityRemarkById(remarkId);
        ReturnObject returnObject = new ReturnObject();
        if(returnInt>0){
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        }else{
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage(Contants.SYSTEM_BUSY);
        }
        return returnObject;
    }

    @RequestMapping("/workbench/activity/updateActivityRemarkById.do")
    public @ResponseBody Object updateActivityRemark(ActivityRemark activityRemark,HttpSession session){
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        activityRemark.setEditTime(DateUtils.formateDateTime(new Date()));
        activityRemark.setEditBy(user.getId());
        activityRemark.setEditFlag(Contants.RETURN_OBJECT_CODE_SUCCESS);
        ReturnObject returnObject = new ReturnObject();
        try {
            int returnInt =  activityRemarkService.saveEditActivityRemark(activityRemark);
            if(returnInt>0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                returnObject.setRetData(activityRemark);
            }else{
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
