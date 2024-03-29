package com.xufeng.crm.workbench.mapper;

import com.xufeng.crm.workbench.domain.ActivityRemark;

import java.util.List;

public interface ActivityRemarkMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity_remark
     *
     * @mbggenerated Wed Oct 26 20:00:51 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity_remark
     *
     * @mbggenerated Wed Oct 26 20:00:51 CST 2022
     */
    int insert(ActivityRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity_remark
     *
     * @mbggenerated Wed Oct 26 20:00:51 CST 2022
     */
    int insertSelective(ActivityRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity_remark
     *
     * @mbggenerated Wed Oct 26 20:00:51 CST 2022
     */
    ActivityRemark selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity_remark
     *
     * @mbggenerated Wed Oct 26 20:00:51 CST 2022
     */
    int updateByPrimaryKeySelective(ActivityRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity_remark
     *
     * @mbggenerated Wed Oct 26 20:00:51 CST 2022
     */
    int updateByPrimaryKey(ActivityRemark record);

    /**
     * 通过id查询市场活动备注列表
     * @param id
     * @return
     */
    List<ActivityRemark> selectActivityRemarkForDetailByActivityId(String id);

    /**
     * 插入一条备注
     * @param activityRemark
     * @return
     */
    int insertCreateActivityRemark(ActivityRemark activityRemark);

    /**
     * 通过id删除一条备注
     * @param remarkId
     * @return
     */
    int deleteActivityRemarkById(String remarkId);

    /**
     * 更新活动备注
     * @param activityRemark
     * @return
     */
    int updateActivityRemark(ActivityRemark activityRemark);
}