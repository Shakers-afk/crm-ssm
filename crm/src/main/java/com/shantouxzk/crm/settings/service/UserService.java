package com.shantouxzk.crm.settings.service;

import com.shantouxzk.crm.settings.domain.User;
import com.shantouxzk.crm.settings.exception.LoginException;

public interface UserService {
    User login(String loginAct,String loginPwd,String ip) throws LoginException;
}
