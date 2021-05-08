package com.shantouxzk.crm.workbench.service.impl;

import com.shantouxzk.crm.workbench.dao.ActivityDao;
import com.shantouxzk.crm.workbench.domain.Activity;
import com.shantouxzk.crm.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ActivityServiceImpl implements ActivityService {
    @Autowired
    private ActivityDao dao;

    @Override
    public Boolean save(Activity activity){
        dao.save(activity);
        return true;
    }
}
