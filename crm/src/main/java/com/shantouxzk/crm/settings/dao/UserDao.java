package com.shantouxzk.crm.settings.dao;

import com.shantouxzk.crm.settings.domain.User;

import java.util.List;

public interface UserDao {
    User selectUser(String loginAct,String loginPwd);
    List<User> selectUsers();
}
