package com.xufeng.crm.workbench.service;

import com.xufeng.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityService {
    int saveCreateActivity(Activity activity);


    List<Activity> queryActivityByConditionForPage(Map<String, Object> map);

    int queryCountOfActivityByCondition(Map<String, Object> map);

    int deleteActivityByIds(String[] ids);

    Activity queryActivityById(String id);

    int updateActivityById(Activity activity);

    List<Activity> queryAllActivitys();

    int saveCreateActivityByList(List<Activity> activityList);

    Activity queryActivityForDetail(String id);
}
