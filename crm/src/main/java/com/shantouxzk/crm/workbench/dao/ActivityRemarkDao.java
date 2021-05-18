package com.shantouxzk.crm.workbench.dao;

import com.shantouxzk.crm.workbench.domain.Activity;
import com.shantouxzk.crm.workbench.domain.ActivityRemark;

import java.util.List;

public interface ActivityRemarkDao {
    int deleteByAids(String[] aidArr);

    List<ActivityRemark> getRemarkListByAid(String activityId);

    void deleteById(String id);

    void saveRemark(ActivityRemark activityRemark);

    void updateRemark(ActivityRemark activityRemark);

}
