package com.shantouxzk.crm.settings.dao;

import com.shantouxzk.crm.settings.domain.User;

public interface UserDao {
    User login(String loginAct,String loginPwd);
}
