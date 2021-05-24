package com.shantouxzk.crm.workbench.dao;

import com.shantouxzk.crm.workbench.domain.ActivityRemark;

import java.util.List;

public interface ActivityRemarkDao {
    int deleteByAids(String[] aids);

    int getCountByAids(String[] aids);

    List<ActivityRemark> getRemarkListByAid(String activityId);

    int deleteById(String id);

    int saveRemark(ActivityRemark activityRemark);

    int updateRemark(ActivityRemark activityRemark);

}
