package com.xufeng.crm.workbench.service.impl;

import com.xufeng.crm.workbench.domain.Activity;
import com.xufeng.crm.workbench.mapper.ActivityMapper;
import com.xufeng.crm.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActivityServiceImpl implements ActivityService {
    @Autowired
    private ActivityMapper activityMapper;
    @Override
    public int saveCreateActivity(Activity activity) {
        return activityMapper.insertactivity(activity);
    }
}
