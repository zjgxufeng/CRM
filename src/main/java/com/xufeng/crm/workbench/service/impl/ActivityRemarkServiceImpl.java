package com.xufeng.crm.workbench.service.impl;

import com.xufeng.crm.workbench.domain.ActivityRemark;
import com.xufeng.crm.workbench.mapper.ActivityRemarkMapper;
import com.xufeng.crm.workbench.service.ActivityRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityRemarkServiceImpl implements ActivityRemarkService {
    @Autowired
    private ActivityRemarkMapper activityRemarkMapper;

    @Override
    public int deleteActivityRemarkById(String remarkId) {
        return activityRemarkMapper.deleteActivityRemarkById(remarkId);
    }

    @Override
    public List<ActivityRemark> queryActivityRemarkForDetailByActivityId(String id) {
        return activityRemarkMapper.selectActivityRemarkForDetailByActivityId(id);
    }

    @Override
    public int saveCreateActivityRemark(ActivityRemark activityRemark) {
        return activityRemarkMapper.insertCreateActivityRemark(activityRemark);
    }

    @Override
    public int saveEditActivityRemark(ActivityRemark activityRemark) {
        return activityRemarkMapper.updateActivityRemark(activityRemark);
    }


}
