package com.shantouxzk.crm.workbench.web.controller;

import com.shantouxzk.crm.settings.domain.User;
import com.shantouxzk.crm.settings.service.UserService;
import com.shantouxzk.crm.utils.DateTimeUtil;
import com.shantouxzk.crm.utils.UUIDUtil;
import com.shantouxzk.crm.vo.PaginationVo;
import com.shantouxzk.crm.workbench.domain.Activity;
import com.shantouxzk.crm.workbench.domain.ActivityRemark;
import com.shantouxzk.crm.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/workbench/activity")
public class ActivityController {
    private UserService userService;
    private ActivityService activityService;
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    @Autowired
    public void setActivityService(ActivityService activityService) {
        this.activityService = activityService;
    }

    @RequestMapping("/getUserList.do")
    @ResponseBody
    public List<User> doGetUserList(){
        return userService.getUserList();
    }

    @RequestMapping("/getUserListAndActivity.do")
    @ResponseBody
    public Map<String,Object> doGetUsersAndActivity(String id){
        return activityService.getUserListAndActivity(id);
    }

    @RequestMapping("/getRemarkListByAid.do")
    @ResponseBody
    public List<ActivityRemark> doGetRemarkListByAid(String activityId){
        return activityService.getRemarkListByAid(activityId);
    }

    @RequestMapping("/save.do")
    @ResponseBody
    public Map<String,Object> doSave(HttpServletRequest request, Activity activity){
        Map<String,Object> map = new HashMap<>();

        activity.setId(UUIDUtil.getUUID());
        activity.setCreateBy(((User)request.getSession().getAttribute("user")).getName());
        activity.setCreateTime(DateTimeUtil.getSysTime());

        Boolean success = activityService.save(activity);
        map.put("success",success);
        return map;
    }

    @RequestMapping("/delete.do")
    @ResponseBody
    public Map<String,Object> doDelete(String[] id){
        Map<String,Object> map = new HashMap<>();
        boolean success = activityService.delete(id);
        map.put("success",success);
        return map;
    }

    @RequestMapping("/update.do")
    @ResponseBody
    public Map<String,Object> doUpdate(HttpServletRequest request, Activity activity){
        Map<String,Object> map = new HashMap<>();

        activity.setEditTime(DateTimeUtil.getSysTime());
        activity.setEditBy(((User)request.getSession().getAttribute("user")).getName());

        Boolean success = activityService.update(activity);
        map.put("success",success);
        return map;
    }

    @RequestMapping("/deleteRemark.do")
    @ResponseBody
    public Map<String,Object> doDeleteRemark(String id){
        Map<String,Object> map = new HashMap<>();
        Boolean success = activityService.deleteRemark(id);
        map.put("success",success);
        return map;
    }

    @RequestMapping("/saveRemark.do")
    @ResponseBody
    public Map<String,Object> doSaveRemark(HttpServletRequest request, ActivityRemark activityRemark){
        Map<String,Object> map = new HashMap<>();

        activityRemark.setId(UUIDUtil.getUUID());
        activityRemark.setCreateBy(((User)request.getSession().getAttribute("user")).getName());
        activityRemark.setCreateTime(DateTimeUtil.getSysTime());
        activityRemark.setEditFlag("0");
        Boolean success = activityService.saveRemark(activityRemark);
        map.put("success",success);
        map.put("activityRemark",activityRemark);
        return map;
    }

    @RequestMapping("/updateRemark.do")
    @ResponseBody
    public Map<String,Object> doUpdateRemark(HttpServletRequest request, ActivityRemark activityRemark){
        Map<String,Object> map = new HashMap<>();
        activityRemark.setEditBy(((User)request.getSession().getAttribute("user")).getName());
        activityRemark.setEditTime(DateTimeUtil.getSysTime());
        activityRemark.setEditFlag("1");
        Boolean success = activityService.updateRemark(activityRemark);
        map.put("success",success);
        map.put("activityRemark",activityRemark);
        return map;
    }

    @RequestMapping("/pageList.do")
    @ResponseBody
    public PaginationVo<Activity> doPageList(Integer pageNo,Integer pageSize,Activity activity){
        return activityService.pageList(pageNo,pageSize,activity);
    }

    @RequestMapping("/detail.do")
    public ModelAndView doDetail(String id){
        ModelAndView mv = new ModelAndView();
        Activity activity = activityService.detail(id);
        mv.addObject("activity",activity);

        mv.setViewName("/workbench/activity/detail.jsp");
        return mv;
    }
}
