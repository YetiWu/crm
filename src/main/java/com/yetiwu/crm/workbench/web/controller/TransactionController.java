package com.yetiwu.crm.workbench.web.controller;

import com.yetiwu.crm.settings.domain.User;
import com.yetiwu.crm.settings.service.UserService;
import com.yetiwu.crm.settings.service.impl.UserServiceImpl;
import com.yetiwu.crm.utils.DateTimeUtil;
import com.yetiwu.crm.utils.PrintJson;
import com.yetiwu.crm.utils.ServiceFactory;
import com.yetiwu.crm.utils.UUIDUtil;
import com.yetiwu.crm.vo.PaginationVo;
import com.yetiwu.crm.workbench.domain.Activity;
import com.yetiwu.crm.workbench.domain.Contacts;
import com.yetiwu.crm.workbench.domain.Tran;
import com.yetiwu.crm.workbench.domain.TranHistory;
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

public class TransactionController extends HttpServlet{

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到交易模块中");
        String path = request.getServletPath();
        if("/workbench/transaction/getOwnerList.do".equals(path)){
            getOwnerList(request,response);
        }else if("/workbench/transaction/getActivityList.do".equals(path)){
            getActivityList(request,response);
        }else if("/workbench/transaction/getContactsNameList.do".equals(path)){
            getContactsNameList(request,response);
        }else if("/workbench/transaction/getCustomerName.do".equals(path)){
            getCustomerName(request,response);
        }else if("/workbench/transaction/save.do".equals(path)){
            save(request,response);
        }else if("/workbench/transaction/pageList.do".equals(path)){
            pageList(request,response);
        }else if("/workbench/transaction/detail.do".equals(path)){
            detail(request,response);
        }else if("/workbench/transaction/getHistoryByTranId.do".equals(path)){
            getHistoryByTranId(request,response);
        }else if("/workbench/transaction/changeStage.do".equals(path)){
            changeStage(request,response);
        }else if("/workbench/transaction/getCharts.do".equals(path)){
            getCharts(request,response);
        }
    }

    private void getCharts(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("取得charts");

        TransactionService service = (TransactionService) ServiceFactory.getService(new TransactionServiceImpl());
        Map<String,Object> map = service.getCharts();

        PrintJson.printJsonObj(response,map);
    }


    private void changeStage(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行修改交易阶段操作");
        String id = request.getParameter("id");
        String stage = request.getParameter("stage");
        String money = request.getParameter("money");
        String expectedDate = request.getParameter("expectedDate");
        String editBy = ((User)request.getSession().getAttribute("user")).getName();
        String editTime = DateTimeUtil.getSysTime();

        Tran tran = new Tran();
        tran.setId(id);
        tran.setStage(stage);
        tran.setExpectedDate(expectedDate);
        tran.setMoney(money);
        tran.setEditBy(editBy);
        tran.setEditTime(editTime);

        ServletContext application = request.getServletContext();
        Map<String,String> pMap = (Map<String, String>) application.getAttribute("pMap");
        tran.setPossibility(pMap.get(stage));

        TransactionService service = (TransactionService) ServiceFactory.getService(new TransactionServiceImpl());

        boolean flag = service.changeStage(tran);

        Map<String,Object> map = new HashMap<>();
        map.put("success",flag);
        map.put("tran",tran);
        PrintJson.printJsonObj(response,map);

        }

    private void getHistoryByTranId(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("获取交易历史");
        String id = request.getParameter("id");
        TransactionService service = (TransactionService) ServiceFactory.getService(new TransactionServiceImpl());
        List<TranHistory> tranHistoryList = service.getHistoryByTranId(id);
        ServletContext application = request.getServletContext();
        Map<String,String> pMap = (Map<String, String>) application.getAttribute("pMap");
        for(TranHistory tranHistory : tranHistoryList){
            String stage = tranHistory.getStage();
            String possibiliity = pMap.get(stage);
            tranHistory.setPossibility(possibiliity);
        }

        PrintJson.printJsonObj(response,tranHistoryList);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到详细页面");
        String id = request.getParameter("id");
        TransactionService service = (TransactionService) ServiceFactory.getService(new TransactionServiceImpl());
        Tran tran = service.detail(id);

        ServletContext application = request.getServletContext();
        Map<String,String> pMap = (Map<String, String>) application.getAttribute("pMap");
        String possibility = pMap.get(tran.getStage());
        tran.setPossibility(possibility);
        request.setAttribute("tran",tran);
        request.getRequestDispatcher("/workbench/transaction/detail.jsp").forward(request,response);
    }

    private void pageList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到展现交易列表操作");
        String pageNoStr = request.getParameter("pageNo");
        String pageSizeStr = request.getParameter("pageSize");
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String customerName = request.getParameter("customerName");
        String stage = request.getParameter("stage");
        String type = request.getParameter("type");
        String source = request.getParameter("source");
        String contactsName = request.getParameter("contactsName");

        Integer pageNo = Integer.valueOf(pageNoStr);
        Integer pageSize = Integer.valueOf(pageSizeStr);

        Integer skipCount = (pageNo - 1) * pageSize;

        Map<String,Object> map = new HashMap<>();
        map.put("name",name);
        map.put("stage",stage);
        map.put("type",type);
        map.put("source",source);
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);
        map.put("owner",owner);
        map.put("customerName",customerName);
        map.put("contactsName",contactsName);

        TransactionService service = (TransactionService) ServiceFactory.getService(new TransactionServiceImpl());
        PaginationVo vo = service.pageList(map);
        PrintJson.printJsonObj(response,vo);
    }
    private void save(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("执行添加交易操作");
        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String money = request.getParameter("money");
        String name = request.getParameter("name");
        String expectedDate = request.getParameter("expectedDate");
        String customerName = request.getParameter("customerName");
        String stage = request.getParameter("stage");
        String type = request.getParameter("type");
        String source = request.getParameter("source");
        String activityId = request.getParameter("activityId");
        String contactsId = request.getParameter("contactsId");
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String createTime = DateTimeUtil.getSysTime();
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");

        Tran tran = new Tran();
        tran.setId(id);
        tran.setOwner(owner);
        tran.setMoney(money);
        tran.setName(name);
        tran.setExpectedDate(expectedDate);
        tran.setStage(stage);
        tran.setType(type);
        tran.setSource(source);
        tran.setActivityId(activityId);
        tran.setContactsId(contactsId);
        tran.setCreateBy(createBy);
        tran.setCreateTime(createTime);
        tran.setDescription(description);
        tran.setContactSummary(contactSummary);
        tran.setNextContactTime(nextContactTime);

        TransactionService service = (TransactionService) ServiceFactory.getService(new TransactionServiceImpl());

        boolean flag = service.save(tran,customerName);
        response.sendRedirect(request.getContextPath() + "/workbench/transaction/index.jsp");
    }

    private void getCustomerName(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入自动补全操作");
        String name = request.getParameter("name");
        CustomerService service = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());
        List<String> customerName = service.getCustomerName(name);
        PrintJson.printJsonObj(response,customerName);

    }

    private void getContactsNameList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到获取联系人名称列表页面");
        String name = request.getParameter("name");

        ContactsService service = (ContactsService) ServiceFactory.getService(new ContactsServiceImpl());
        List<Contacts> contactsList = service.getContactsNameList(name);
        PrintJson.printJsonObj(response,contactsList);
    }

    private void getActivityList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到获取市场活动列表界面");
        String name = request.getParameter("name");
        ActivityService service = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<Activity> activityList = service.getActivityListByName(name);
        PrintJson.printJsonObj(response,activityList);
    }

    private void getOwnerList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到跳转到创建交易页面操作");
        UserService service = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> userList = service.getUserList();

        request.setAttribute("userList",userList);

        request.getRequestDispatcher("/workbench/transaction/save.jsp").forward(request,response);
    }


}
