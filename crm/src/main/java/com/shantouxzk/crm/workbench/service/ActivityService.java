package com.shantouxzk.crm.workbench.service;

import com.shantouxzk.crm.vo.PaginationVo;
import com.shantouxzk.crm.workbench.domain.Activity;

import java.util.Map;

public interface ActivityService {
    Boolean saveActivity(Activity activity);
    Boolean removeActivities(String[] idArr);

    PaginationVo<Activity> pageList(Integer pageNo,Integer pageSize,Activity activity);


}
