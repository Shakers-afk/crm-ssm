package com.shantouxzk.crm.settings.web.controller;

import com.shantouxzk.crm.settings.domain.User;
import com.shantouxzk.crm.exception.LoginException;
import com.shantouxzk.crm.settings.service.UserService;
import com.shantouxzk.crm.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/settings/user")
public class UserController {
    @Autowired
    private UserService service;

    @RequestMapping(value = "/login.do",method = RequestMethod.POST,produces = "application/json")
    @ResponseBody
    public Map<String,Object> doLogin(HttpServletRequest request, HttpServletResponse response, User user) throws LoginException {
        String loginAct = user.getLoginAct();
        String loginPwd = user.getLoginPwd();
        String ip = request.getRemoteAddr();
        loginPwd = MD5Util.getMD5(loginPwd);

        Map<String,Object> map = new HashMap<>();
        user = service.login(loginAct,loginPwd,ip);
        request.getSession().setAttribute("user",user);
        map.put("success",true);
        return map;
    }
}
