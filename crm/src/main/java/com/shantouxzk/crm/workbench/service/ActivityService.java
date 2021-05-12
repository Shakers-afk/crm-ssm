package com.shantouxzk.crm.workbench.service;

import com.shantouxzk.crm.vo.PaginationVo;
import com.shantouxzk.crm.workbench.domain.Activity;

import java.util.Map;

public interface ActivityService {
    Boolean save(Activity activity);
    Boolean delete(String[] idArr);
    Boolean update(Activity activity);
    PaginationVo<Activity> pageList(Integer pageNo,Integer pageSize,Activity activity);

    Map<String,Object> getUserListAndActivity(String id);


}
