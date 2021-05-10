package com.shantouxzk.crm.workbench.dao;

import com.shantouxzk.crm.workbench.domain.Activity;

import java.util.List;

public interface ActivityDao {
    int insertActivity(Activity activity);
    int deleteActivitiesByIds(String[] idArr);
    List<Activity> selectActivitiesByCondition(Activity activity);
}
