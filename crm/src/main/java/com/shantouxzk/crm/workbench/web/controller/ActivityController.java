package com.shantouxzk.crm.workbench.web.controller;

import com.shantouxzk.crm.settings.domain.User;
import com.shantouxzk.crm.settings.service.UserService;
import com.shantouxzk.crm.utils.DateTimeUtil;
import com.shantouxzk.crm.utils.UUIDUtil;
import com.shantouxzk.crm.vo.PaginationVo;
import com.shantouxzk.crm.workbench.dao.ActivityRemarkDao;
import com.shantouxzk.crm.workbench.domain.Activity;
import com.shantouxzk.crm.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/workbench/activity")
public class ActivityController {
    @Autowired
    private UserService userService;
    @Autowired
    private ActivityService activityService;

    @RequestMapping("/getUserList.do")
    @ResponseBody
    public List<User> doGetUserList(){
        List<User> users = userService.getUserList();
        return users;
    }

    @RequestMapping("/save.do")
    @ResponseBody
    public Map<String,Object> doSave(HttpServletRequest request, Activity activity){
        Map<String,Object> map = new HashMap<>();
        String id = UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();

        activity.setId(id);
        activity.setCreateBy(createBy);
        activity.setCreateTime(createTime);

        Boolean flag = activityService.saveActivity(activity);
        map.put("success",flag);
        return map;
    }

    @RequestMapping("/pageList.do")
    @ResponseBody
    public PaginationVo<Activity> doPageList(Integer pageNo,Integer pageSize,Activity activity){
        PaginationVo<Activity> vo = activityService.pageList(pageNo,pageSize,activity);
        return vo;
    }

    @RequestMapping("/delete.do")
    @ResponseBody
    public Map<String,Object> doDelete(String[] id){
        Map<String,Object> map = new HashMap<>();
//        String[] idArr = id.split("&");

        boolean flag = activityService.removeActivities(id);
        map.put("success",flag);
        return map;
    }
}
