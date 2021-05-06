package com.shantouxzk.crm.settings.service.impl;

import com.shantouxzk.crm.settings.dao.UserDao;
import com.shantouxzk.crm.settings.service.UserService;
import com.shantouxzk.crm.utils.SqlSessionUtil;

public class UserServiceImpl implements UserService {
    private UserDao dao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

}
