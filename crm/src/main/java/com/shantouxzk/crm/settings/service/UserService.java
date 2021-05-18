package com.shantouxzk.crm.settings.service;

import com.shantouxzk.crm.settings.domain.User;
import com.shantouxzk.crm.exception.LoginException;
import com.shantouxzk.crm.workbench.domain.Clue;

import java.util.List;

public interface UserService {
    User login(String loginAct,String loginPwd,String ip) throws LoginException;
    List<User> getUserList();


}
