package com.shantouxzk.crm.workbench.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shantouxzk.crm.settings.dao.UserDao;
import com.shantouxzk.crm.settings.domain.User;
import com.shantouxzk.crm.vo.PaginationVo;
import com.shantouxzk.crm.workbench.dao.ActivityDao;
import com.shantouxzk.crm.workbench.dao.ActivityRemarkDao;
import com.shantouxzk.crm.workbench.domain.Activity;
import com.shantouxzk.crm.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class ActivityServiceImpl implements ActivityService {
    @Autowired
    private ActivityDao activityDao;
    @Autowired
    private ActivityRemarkDao activityRemarkDao;
    @Autowired
    private UserDao userDao;

    @Override
    public Boolean save(Activity activity){
        activityDao.save(activity);
        return true;
    }

    @Transactional
    @Override
    public Boolean delete(String[] idArr) {
        //删除市场活动相关备注
        activityRemarkDao.deleteByAids(idArr);
        activityDao.delete(idArr);
        return true;
    }

    @Override
    public Map<String, Object> getUserListAndActivity(String id) {
        Map<String,Object> map = new HashMap<>();
        Activity activity = activityDao.getById(id);
        List<User> userList = userDao.getUserList();
        map.put("userList",userList);
        map.put("activity",activity);
        return map;
    }

    @Override
    public Boolean update(Activity activity) {
        activityDao.update(activity);
        return true;
    }

    @Override
    public PaginationVo<Activity> pageList(Integer pageNo, Integer pageSize, Activity activity) {
        PaginationVo<Activity> vo = new PaginationVo<>();

        PageHelper.startPage(pageNo,pageSize);
        List<Activity> dataList = activityDao.getActivityListByCondition(activity);
        PageInfo<Activity> info = new PageInfo<>(dataList);

        int total = (int) info.getTotal();
        vo.setTotal(total);
        vo.setDataList(dataList);
        return vo;
    }


}
