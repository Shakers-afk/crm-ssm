package com.shantouxzk.crm.workbench.web.controller;

import com.shantouxzk.crm.settings.domain.User;
import com.shantouxzk.crm.settings.service.UserService;
import com.shantouxzk.crm.utils.DateTimeUtil;
import com.shantouxzk.crm.utils.UUIDUtil;
import com.shantouxzk.crm.workbench.domain.Tran;
import com.shantouxzk.crm.workbench.domain.TranHistory;
import com.shantouxzk.crm.workbench.service.CustomerService;
import com.shantouxzk.crm.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("/workbench/transaction")
public class TranController {
    private TranService tranService;
    private UserService userService;
    private CustomerService customerService;

    @Autowired
    public void setTranService(TranService tranService) {
        this.tranService = tranService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    @RequestMapping("/add.do")
    public ModelAndView doAdd() {
        ModelAndView mv = new ModelAndView();
        List<User> userList = userService.getUserList();
        mv.addObject("userList", userList);
        mv.setViewName("/workbench/transaction/save.jsp");
        return mv;
    }

    @RequestMapping("/getCustomerName.do")
    @ResponseBody
    public List<String> doGetCustomerName(String name) {
        return customerService.getCustomerName(name);
    }

    @RequestMapping("/save.do")
    public ModelAndView doSave(Tran tran, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        String customerName = request.getParameter("customerName");

        tran.setId(UUIDUtil.getUUID());
        tran.setCreateBy(((User) request.getSession().getAttribute("user")).getName());
        tran.setCreateTime(DateTimeUtil.getSysTime());
        Boolean success = tranService.save(tran, customerName);

        if (success) {
            mv.setViewName("redirect:/workbench/transaction/index.jsp");
        }
        return mv;
    }

    @RequestMapping("/detail.do")
    @SuppressWarnings("unchecked")
    public ModelAndView doDetail(String id) {
        ModelAndView mv = new ModelAndView();
        Tran tran = tranService.detail(id);

        ServletContext application = Objects.requireNonNull(ContextLoader.getCurrentWebApplicationContext()).getServletContext();
        assert application != null;
        Map<String, String> pMap = (Map<String, String>) application.getAttribute("pMap");
        String stage = tran.getStage();

        String possibility = pMap.get(stage);
        tran.setPossibility(possibility);

        mv.addObject("tran", tran);
        mv.setViewName("forward:/workbench/transaction/detail.jsp");
        return mv;
    }

    @RequestMapping("/getHistoryListByTranId.do")
    @ResponseBody
    @SuppressWarnings("unchecked")
    public List<TranHistory> doGetHistoryListByTranId(String tranId) {
        List<TranHistory> tranHistoryList = tranService.getHistoryListByTranId(tranId);

        ServletContext application = Objects.requireNonNull(ContextLoader.getCurrentWebApplicationContext()).getServletContext();
        assert application != null;
        Map<String, String> pMap = (Map<String, String>) application.getAttribute("pMap");
        for (TranHistory tranHistory : tranHistoryList) {
            String stage = tranHistory.getStage();
            String possibility = pMap.get(stage);
            tranHistory.setPossibility(possibility);
        }
        return tranHistoryList;
    }

    @RequestMapping("/changeStage.do")
    @SuppressWarnings("unchecked")
    @ResponseBody
    public Map<String, Object> doChangeStage(Tran tran, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        tran.setEditBy(((User) request.getSession().getAttribute("user")).getName());
        tran.setEditTime(DateTimeUtil.getSysTime());
        Boolean success = tranService.changeStage(tran);

        ServletContext application = Objects.requireNonNull(ContextLoader.getCurrentWebApplicationContext()).getServletContext();
        assert application != null;
        Map<String, String> pMap = (Map<String, String>) application.getAttribute("pMap");
        String possibility = pMap.get(tran.getStage());
        tran.setPossibility(possibility);

        map.put("success", success);
        map.put("tran", tran);
        return map;
    }

    @RequestMapping("/getCharts.do")
    @ResponseBody
    public Map<String,Object> doGetCharts(){
        return tranService.getCharts();
    }
}
