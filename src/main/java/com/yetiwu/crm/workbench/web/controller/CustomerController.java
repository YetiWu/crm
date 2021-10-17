package com.yetiwu.crm.workbench.web.controller;

import com.yetiwu.crm.settings.domain.User;
import com.yetiwu.crm.settings.service.UserService;
import com.yetiwu.crm.settings.service.impl.UserServiceImpl;
import com.yetiwu.crm.utils.DateTimeUtil;
import com.yetiwu.crm.utils.PrintJson;
import com.yetiwu.crm.utils.ServiceFactory;
import com.yetiwu.crm.utils.UUIDUtil;
import com.yetiwu.crm.vo.PaginationVo;
import com.yetiwu.crm.workbench.domain.*;
import com.yetiwu.crm.workbench.service.ActivityService;
import com.yetiwu.crm.workbench.service.ContactsService;
import com.yetiwu.crm.workbench.service.CustomerService;
import com.yetiwu.crm.workbench.service.TransactionService;
import com.yetiwu.crm.workbench.service.impl.ActivityServiceImpl;
import com.yetiwu.crm.workbench.service.impl.ContactsServiceImpl;
import com.yetiwu.crm.workbench.service.impl.CustomerServiceImpl;
import com.yetiwu.crm.workbench.service.impl.TransactionServiceImpl;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerController extends HttpServlet{

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到客户模块中");
        String path = request.getServletPath();
        if("/workbench/customer/getOwnerList.do".equals(path)){
            getOwnerList(request,response);
        }else if("/workbench/customer/xxx.do".equals(path)){
            //xxx(request,response);
        }
    }

    private void getOwnerList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到创建模态窗口中获取所有者信息操作");
        UserService service = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> userList = service.getUserList();
        PrintJson.printJsonObj(response,userList);
    }
}
