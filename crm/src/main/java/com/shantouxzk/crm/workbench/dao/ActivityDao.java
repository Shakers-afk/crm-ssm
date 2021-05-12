package com.shantouxzk.crm.workbench.dao;

import com.shantouxzk.crm.workbench.domain.Activity;

import java.util.List;

public interface ActivityDao {
    void save(Activity activity);
    void delete(String[] idArr);
    void update(Activity activity);
    Activity getById(String id);
    List<Activity> getActivityListByCondition(Activity activity);

}
