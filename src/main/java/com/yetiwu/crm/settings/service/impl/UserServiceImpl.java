package com.yetiwu.crm.settings.service.impl;

import com.yetiwu.crm.exception.LoginException;
import com.yetiwu.crm.settings.dao.UserDao;
import com.yetiwu.crm.settings.domain.User;
import com.yetiwu.crm.settings.service.UserService;
import com.yetiwu.crm.utils.DateTimeUtil;
import com.yetiwu.crm.utils.SqlSessionUtil;

import java.util.List;

public class UserServiceImpl implements UserService {
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    @Override
    public User login(String loginAct, String loginPwd, String ip) throws LoginException {
        User user  = new User();

        user.setLoginAct(loginAct);

        user.setLoginPwd(loginPwd);

        User res = userDao.login(user);

        if(res == null){
            throw new LoginException("账号不存在或密码错误");
        }

        String allowIps = res.getAllowIps();
        if(!allowIps.contains(ip)){
            throw new LoginException("ip地址受限");
        }

        String expireTime = res.getExpireTime();
        String currentTime = DateTimeUtil.getSysTime();
        if(expireTime.compareTo(currentTime) < 0 ){
            throw new LoginException("账号已失效");
        }

        String lockState = res.getLockState();
        if("0".equals(lockState)){
            throw new LoginException("账号已锁定，请联系管理员");
        }

        return res;
    }

    @Override
    public List<User> getUserList() {

        List<User> userList = userDao.getUserList();
        return userList;
    }
}
