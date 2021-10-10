package com.yetiwu.crm.settings.web.controller;

import com.yetiwu.crm.settings.domain.User;
import com.yetiwu.crm.settings.service.UserService;
import com.yetiwu.crm.settings.service.impl.UserServiceImpl;
import com.yetiwu.crm.utils.MD5Util;
import com.yetiwu.crm.utils.PrintJson;
import com.yetiwu.crm.utils.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到crm项目中");
        String path = request.getServletPath();
        if("/settings/user/login.do".equals(path)){

            login(request,response);

        }else if("/settings/user/xxx.do".equals(path)){

        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response) {
        // 获取请求参数
        String loginAct = request.getParameter("loginAct");
        String loginPwd = request.getParameter("loginPwd");

        // 使用MD5将密码加密
        loginPwd = MD5Util.getMD5(loginPwd);

        // 获取当前浏览器的ip地址
        String ip = request.getRemoteAddr();
        // 调用业务层来处理结果
        UserService service = (UserService) ServiceFactory.getService(new UserServiceImpl());
        try{
            User user = service.login(loginAct,loginPwd,ip);
            // 将用户放到会话作用域中
            request.getSession().setAttribute("user",user);
            // 使用工具类来将json对象返回给ajax
            PrintJson.printJsonFlag(response,true);
        }catch(Exception e){
            e.printStackTrace();
            String msg = e.getMessage();
            Map<String,Object> map = new HashMap<>();
            map.put("success",false);
            map.put("msg",msg);
            PrintJson.printJsonObj(response,map);
        }
    }
}
