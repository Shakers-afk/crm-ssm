package com.shantouxzk.crm.workbench.web.controller;

import com.shantouxzk.crm.settings.domain.User;
import com.shantouxzk.crm.settings.service.UserService;
import com.shantouxzk.crm.utils.DateTimeUtil;
import com.shantouxzk.crm.utils.UUIDUtil;
import com.shantouxzk.crm.workbench.domain.Activity;
import com.shantouxzk.crm.workbench.domain.Clue;
import com.shantouxzk.crm.workbench.service.ActivityService;
import com.shantouxzk.crm.workbench.service.ClueService;
import org.apache.ibatis.annotations.Param;
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
@RequestMapping("/workbench/clue")
public class ClueController {
    private UserService userService;
    private ActivityService activityService;
    private ClueService clueService;
    @Autowired
    public void setUserService(UserService userService){
        this.userService = userService;
    }
    @Autowired
    public void setClueDao(ClueService clueService){
        this.clueService = clueService;
    }
    @Autowired
    public void setActivityService(ActivityService activityService){
        this.activityService = activityService;
    }

    @RequestMapping("/getUserList.do")
    @ResponseBody
    public List<User> doGetUserList(){
        return userService.getUserList();
    }

    @RequestMapping("/getActivityListByClueId.do")
    @ResponseBody
    public List<Activity> doGetActivityListByClueId(String clueId){
       return activityService.getActivityListByClueId(clueId);
    }

    @RequestMapping("/getActivityListByNameAndNotByClueId.do")
    @ResponseBody
    public List<Activity> doGetActivityListByNameAndNotByClueId(String activityName,String clueId){
        Map<String,String> map = new HashMap<>();
        map.put("activityName",activityName);
        map.put("clueId",clueId);
        return activityService.getActivityListByNameAndNotByClueId(map);
    }

    @RequestMapping("/save.do")
    @ResponseBody
    public Map<String,Object> doSave(Clue clue, HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        String id = UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();

        clue.setId(id);
        clue.setCreateBy(createBy);
        clue.setCreateTime(createTime);
        Boolean flag = clueService.save(clue);
        map.put("success",flag);
        return map;
    }

    @RequestMapping("/detail.do")
    public ModelAndView doDetail(String id){
        ModelAndView mv = new ModelAndView();
        Clue clue = clueService.detail(id);
        mv.addObject("clue",clue);
        mv.setViewName("/workbench/clue/detail.jsp");
        return mv;
    }

    @RequestMapping("/bund.do")
    @ResponseBody
    public Map<String,Object> doBund(String clueId,@Param("activityId") String[] activityId){
        Map<String,Object> map = new HashMap<>();
        Boolean flag = clueService.bund(clueId,activityId);
        map.put("success",flag);
        return map;
    }

    @RequestMapping("/unbund.do")
    @ResponseBody
    public Map<String,Object> doUnbund(String id){
        Map<String,Object> map = new HashMap<>();
        Boolean flag = clueService.unbund(id);
        map.put("success",flag);
        return map;
    }
}
