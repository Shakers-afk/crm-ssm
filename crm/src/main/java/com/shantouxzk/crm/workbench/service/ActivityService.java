package com.shantouxzk.crm.workbench.service;

import com.shantouxzk.crm.vo.PaginationVo;
import com.shantouxzk.crm.workbench.domain.Activity;
import com.shantouxzk.crm.workbench.domain.ActivityRemark;

import java.util.List;
import java.util.Map;

public interface ActivityService {
    Boolean save(Activity activity);
    Boolean delete(String[] idArr);
    Boolean update(Activity activity);
    PaginationVo<Activity> pageList(Integer pageNo,Integer pageSize,Activity activity);
    Map<String,Object> getUserListAndActivity(String id);
    Activity detail(String id);

    List<ActivityRemark> getRemarkListByAid(String activityId);

    Boolean deleteRemark(String id);
    Boolean saveRemark(ActivityRemark activityRemark);
    Boolean updateRemark(ActivityRemark activityRemark);

    List<Activity> getActivityListByClueId(String clueId);

    List<Activity> getActivityListByNameAndNotByClueId(Map<String, String> map);
}
