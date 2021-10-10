package com.yetiwu.crm.settings.service;

import com.yetiwu.crm.exception.LoginException;
import com.yetiwu.crm.settings.domain.User;

import java.util.List;

public interface UserService {

    User login(String loginAct, String loginPwd, String ip) throws LoginException;

    List<User> getUserList();
}
