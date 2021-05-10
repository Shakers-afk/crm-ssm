package com.shantouxzk.crm.workbench.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shantouxzk.crm.vo.PaginationVo;
import com.shantouxzk.crm.workbench.dao.ActivityDao;
import com.shantouxzk.crm.workbench.dao.ActivityRemarkDao;
import com.shantouxzk.crm.workbench.domain.Activity;
import com.shantouxzk.crm.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class ActivityServiceImpl implements ActivityService {
    @Autowired
    private ActivityDao activityDao;
    @Autowired
    private ActivityRemarkDao activityRemarkDao;

    @Override
    public Boolean saveActivity(Activity activity){
        activityDao.insertActivity(activity);
        return true;
    }

    @Transactional
    @Override
    public Boolean removeActivities(String[] idArr) {
        //删除市场活动相关备注
        activityRemarkDao.deleteActivityRemarksByAids(idArr);
        activityDao.deleteActivitiesByIds(idArr);
        return true;
    }

    @Override
    public PaginationVo<Activity> pageList(Integer pageNo, Integer pageSize, Activity activity) {
        PaginationVo<Activity> vo = new PaginationVo<>();

        PageHelper.startPage(pageNo,pageSize);
        List<Activity> dataList = activityDao.selectActivitiesByCondition(activity);
        PageInfo<Activity> info = new PageInfo<>(dataList);

        Integer total = (int) info.getTotal();
        vo.setTotal(total);
        vo.setDataList(dataList);
        return vo;
    }


}
