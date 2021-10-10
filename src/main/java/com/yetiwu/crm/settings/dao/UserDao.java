package com.yetiwu.crm.settings.dao;

import com.yetiwu.crm.settings.domain.User;

import java.util.List;

public interface UserDao {
    public User login(User user);

    List<User> getUserList();
}
