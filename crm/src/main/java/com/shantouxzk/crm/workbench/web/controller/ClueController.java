package com.shantouxzk.crm.workbench.web.controller;

import com.shantouxzk.crm.settings.domain.User;
import com.shantouxzk.crm.settings.service.UserService;
import com.shantouxzk.crm.utils.DateTimeUtil;
import com.shantouxzk.crm.utils.UUIDUtil;
import com.shantouxzk.crm.workbench.domain.Activity;
import com.shantouxzk.crm.workbench.domain.Clue;
import com.shantouxzk.crm.workbench.domain.Tran;
import com.shantouxzk.crm.workbench.service.ActivityService;
import com.shantouxzk.crm.workbench.service.ClueService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Shakers
 */
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

    @RequestMapping("/getActivityListByName")
    @ResponseBody
    public List<Activity> doGetActivityListByName(String activityName){
        return activityService.getActivityListByName(activityName);
    }

    @RequestMapping("/save.do")
    @ResponseBody
    public Map<String,Object> doSave(Clue clue, HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        clue.setId(UUIDUtil.getUUID());
        clue.setCreateBy(((User)request.getSession().getAttribute("user")).getName());
        clue.setCreateTime(DateTimeUtil.getSysTime());
        Boolean success = clueService.save(clue);
        map.put("success",success);
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
        Boolean success = clueService.bund(clueId,activityId);
        map.put("success",success);
        return map;
    }

    @RequestMapping("/unbund.do")
    @ResponseBody
    public Map<String,Object> doUnbund(String id){
        Map<String,Object> map = new HashMap<>();
        Boolean success = clueService.unbund(id);
        map.put("success",success);
        return map;
    }

    @RequestMapping("convert.do")
    public ModelAndView doConvert(String clueId, Boolean flag, Tran tran, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        if (flag!=null && flag){
            tran.setId(UUIDUtil.getUUID());
            tran.setCreateTime(DateTimeUtil.getSysTime());
            tran.setCreateBy(createBy);
        }

        Boolean success = clueService.convert(clueId,tran,createBy);
        if (success){
            mv.setViewName("redirect:/workbench/clue/index.jsp");
        }

        return mv;
    }

//    @RequestMapping("/pageList.do")
//    @ResponseBody
//    public List<Clue> doPageList(Integer pageNo, Integer pageSize,Clue clue){
//        return clueService.pageList(pageNo,pageSize,clue);
//    }
}
