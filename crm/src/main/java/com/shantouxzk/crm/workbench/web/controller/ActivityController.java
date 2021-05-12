package com.shantouxzk.crm.workbench.web.controller;

import com.shantouxzk.crm.settings.domain.User;
import com.shantouxzk.crm.settings.service.UserService;
import com.shantouxzk.crm.utils.DateTimeUtil;
import com.shantouxzk.crm.utils.UUIDUtil;
import com.shantouxzk.crm.vo.PaginationVo;
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
    private Map<String,Object> map;

    @RequestMapping("/getUserList.do")
    @ResponseBody
    public List<User> doGetUserList(){
        return userService.getUserList();
    }

    @RequestMapping("/getUserListAndActivity.do")
    @ResponseBody
    public Map<String,Object> doGetUsersAndActivity(String id){
        map = activityService.getUserListAndActivity(id);
        return map;
    }

    @RequestMapping("/save.do")
    @ResponseBody
    public Map<String,Object> doSave(HttpServletRequest request, Activity activity){
        map = new HashMap<>();
        String id = UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();

        activity.setId(id);
        activity.setCreateBy(createBy);
        activity.setCreateTime(createTime);

        Boolean flag = activityService.save(activity);
        map.put("success",flag);
        return map;
    }

    @RequestMapping("/delete.do")
    @ResponseBody
    public Map<String,Object> doDelete(String[] id){
        map = new HashMap<>();

        boolean flag = activityService.delete(id);
        map.put("success",flag);
        return map;
    }

    @RequestMapping("/update.do")
    @ResponseBody
    public Map<String,Object> doUpdate(HttpServletRequest request, Activity activity){
        map = new HashMap<>();
        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User)request.getSession().getAttribute("user")).getName();

        activity.setEditTime(editTime);
        activity.setEditTime(editBy);

        Boolean flag = activityService.update(activity);
        map.put("success",flag);
        return map;
    }

    @RequestMapping("/pageList.do")
    @ResponseBody
    public PaginationVo<Activity> doPageList(Integer pageNo,Integer pageSize,Activity activity){
        return activityService.pageList(pageNo,pageSize,activity);
    }
}
