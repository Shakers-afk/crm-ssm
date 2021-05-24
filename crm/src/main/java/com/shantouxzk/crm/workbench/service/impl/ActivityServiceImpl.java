package com.shantouxzk.crm.workbench.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shantouxzk.crm.settings.dao.UserDao;
import com.shantouxzk.crm.settings.domain.User;
import com.shantouxzk.crm.vo.PaginationVo;
import com.shantouxzk.crm.workbench.dao.ActivityDao;
import com.shantouxzk.crm.workbench.dao.ActivityRemarkDao;
import com.shantouxzk.crm.workbench.domain.Activity;
import com.shantouxzk.crm.workbench.domain.ActivityRemark;
import com.shantouxzk.crm.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class ActivityServiceImpl implements ActivityService {
    private UserDao userDao;
    private ActivityDao activityDao;
    private ActivityRemarkDao activityRemarkDao;

    @Autowired
    public void setActivityDao(ActivityDao activityDao) {
        this.activityDao = activityDao;
    }

    @Autowired
    public void setActivityRemarkDao(ActivityRemarkDao activityRemarkDao) {
        this.activityRemarkDao = activityRemarkDao;
    }

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Boolean save(Activity activity) {
        int count = activityDao.save(activity);
        return count == 1;
    }

    @Transactional
    @Override
    public Boolean delete(String[] ids) {
        boolean flag = true;
        //删除市场活动相关备注
        int count1 = activityRemarkDao.getCountByAids(ids);
        int count2 = activityRemarkDao.deleteByAids(ids);
        if (count1 != count2) {
            flag = false;
        }
        int count3 = activityDao.delete(ids);
        if (count3 != ids.length) {
            flag = false;
        }
        return flag;
    }

    @Override
    public Boolean update(Activity activity) {
        int count = activityDao.update(activity);
        return count == 1;
    }

    @Override
    public Map<String, Object> getUserListAndActivity(String id) {
        Map<String, Object> map = new HashMap<>();
        Activity activity = activityDao.getById(id);
        List<User> userList = userDao.getUserList();
        map.put("userList", userList);
        map.put("activity", activity);
        return map;
    }

    @Override
    public Activity detail(String id) {
        return activityDao.detail(id);
    }

    @Override
    public List<ActivityRemark> getRemarkListByAid(String activityId) {
        return activityRemarkDao.getRemarkListByAid(activityId);
    }

    @Override
    public PaginationVo<Activity> pageList(Integer pageNo, Integer pageSize, Activity activity) {
        PaginationVo<Activity> vo = new PaginationVo<>();

        PageHelper.startPage(pageNo, pageSize);
        List<Activity> dataList = activityDao.getActivityListByCondition(activity);
        PageInfo<Activity> info = new PageInfo<>(dataList);

        int total = (int) info.getTotal();
        vo.setTotal(total);
        vo.setDataList(dataList);
        return vo;
    }

    @Override
    public Boolean deleteRemark(String id) {
        int count = activityRemarkDao.deleteById(id);
        return count == 1;
    }

    @Override
    public Boolean saveRemark(ActivityRemark activityRemark) {
        int count = activityRemarkDao.saveRemark(activityRemark);
        return count == 1;
    }

    @Override
    public Boolean updateRemark(ActivityRemark activityRemark) {
        int count = activityRemarkDao.updateRemark(activityRemark);
        return count == 1;
    }

    @Override
    public List<Activity> getActivityListByClueId(String clueId) {
        return activityDao.getActivityListByClueId(clueId);
    }

    @Override
    public List<Activity> getActivityListByNameAndNotByClueId(Map<String, String> map) {
        return activityDao.getActivityListByNameAndNotByClueId(map);
    }

    @Override
    public List<Activity> getActivityListByName(String activityName) {
        return activityDao.getActivityListByName(activityName);
    }


}
