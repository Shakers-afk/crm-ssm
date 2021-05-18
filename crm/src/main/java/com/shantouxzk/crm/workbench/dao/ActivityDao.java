package com.shantouxzk.crm.workbench.dao;

import com.shantouxzk.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityDao {
    void save(Activity activity);
    void delete(String[] idArr);
    void update(Activity activity);
    Activity getById(String id);
    Activity detail(String id);
    List<Activity> getActivityListByCondition(Activity activity);
    List<Activity> getActivityListByClueId(String clueId);

    List<Activity> getActivityListByNameAndNotByClueId(Map<String, String> map);
}
