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
        Map<String,Object> map = new HashMap<>();
        boolean flag = activityService.delete(id);
        map.put("success",flag);
        return map;
    }

    @RequestMapping("/update.do")
    @ResponseBody
    public Map<String,Object> doUpdate(HttpServletRequest request, Activity activity){
        Map<String,Object> map = new HashMap<>();
        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User)request.getSession().getAttribute("user")).getName();

        activity.setEditTime(editTime);
        activity.setEditBy(editBy);

        Boolean flag = activityService.update(activity);
        map.put("success",flag);
        return map;
    }

    @RequestMapping("/deleteRemark.do")
    @ResponseBody
    public Map<String,Object> doDeleteRemark(String id){
        Map<String,Object> map = new HashMap<>();
        Boolean flag = activityService.deleteRemark(id);
        map.put("success",flag);
        return map;
    }

    @RequestMapping("/saveRemark.do")
    @ResponseBody
    public Map<String,Object> doSaveRemark(HttpServletRequest request, ActivityRemark activityRemark){
        Map<String,Object> map = new HashMap<>();
        String id = UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String editFlag = "0";

        activityRemark.setId(id);
        activityRemark.setCreateBy(createBy);
        activityRemark.setCreateTime(createTime);
        activityRemark.setEditFlag(editFlag);
        Boolean flag = activityService.saveRemark(activityRemark);
        map.put("success",flag);
        map.put("activityRemark",activityRemark);
        return map;
    }

    @RequestMapping("/updateRemark.do")
    @ResponseBody
    public Map<String,Object> doUpdateRemark(HttpServletRequest request, ActivityRemark activityRemark){
        Map<String,Object> map = new HashMap<>();
        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User)request.getSession().getAttribute("user")).getName();
        String editFlag = "1";
        activityRemark.setEditBy(editBy);
        activityRemark.setEditTime(editTime);
        activityRemark.setEditFlag(editFlag);
        Boolean flag = activityService.updateRemark(activityRemark);
        map.put("success",flag);
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
