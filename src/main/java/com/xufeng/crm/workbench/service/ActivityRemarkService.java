package com.xufeng.crm.workbench.service;

import com.xufeng.crm.workbench.domain.ActivityRemark;

import java.util.List;

public interface ActivityRemarkService {
    int deleteActivityRemarkById(String remarkId);


    List<ActivityRemark> queryActivityRemarkForDetailByActivityId(String id);

    int saveCreateActivityRemark(ActivityRemark activityRemark);

    int saveEditActivityRemark(ActivityRemark activityRemark);
}
