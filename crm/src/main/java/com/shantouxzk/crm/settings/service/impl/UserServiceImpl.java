package com.shantouxzk.crm.settings.service.impl;

import com.shantouxzk.crm.settings.dao.UserDao;
import com.shantouxzk.crm.settings.domain.User;
import com.shantouxzk.crm.exception.LoginException;
import com.shantouxzk.crm.settings.service.UserService;
import com.shantouxzk.crm.utils.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao dao;

    @Override
    public User login(String loginAct, String loginPwd, String ip) throws LoginException {
        User user = dao.login(loginAct, loginPwd);
        if (user == null) {
            throw new LoginException("账号不存在或密码错误");
        }
        //判断失效时间
        String expireTime = user.getExpireTime();
        String currentTime = DateTimeUtil.getSysTime();
        if (expireTime.compareTo(currentTime) < 0) {
            throw new LoginException("账号已失效");
        }

        //判断锁定状态
        String lockState = user.getLockState();
        if ("0".equals(lockState)) {
            throw new LoginException("账号已锁定");
        }

        //判断ip地址
        String allowIps = user.getAllowIps();
        if (!allowIps.contains(ip)) {
            throw new LoginException("ip地址受限");
        }

        return user;
    }
}
