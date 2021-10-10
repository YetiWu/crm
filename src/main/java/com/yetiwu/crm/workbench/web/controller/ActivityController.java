package com.yetiwu.crm.workbench.web.controller;

import com.yetiwu.crm.settings.domain.User;
import com.yetiwu.crm.settings.service.UserService;
import com.yetiwu.crm.settings.service.impl.UserServiceImpl;
import com.yetiwu.crm.utils.*;
import com.yetiwu.crm.vo.PaginationVo;
import com.yetiwu.crm.workbench.domain.Activity;
import com.yetiwu.crm.workbench.domain.ActivityRemark;
import com.yetiwu.crm.workbench.service.ActivityService;
import com.yetiwu.crm.workbench.service.impl.ActivityServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到crm项目中");
        String path = request.getServletPath();
        if("/workbench/activity/getUserList.do".equals(path)){
            getUserList(request,response);
        }else if("/workbench/activity/saveActivity.do".equals(path)){
            saveActivity(request,response);
        }else if("/workbench/activity/pageList.do".equals(path)){
            pageList(request,response);
        }else if("/workbench/activity/delete.do".equals(path)){
            delete(request,response);
        }else if("/workbench/activity/getUserListAndActicity.do".equals(path)){
            getUserListAndActicity(request,response);
        }else if("/workbench/activity/update.do".equals(path)){
            update(request,response);
        }else if("/workbench/activity/detail.do".equals(path)){
            detail(request,response);
        }else if("/workbench/activity/getRemarkList.do".equals(path)){
            getRemarkList(request,response);
        }else if("/workbench/activity/deleteRemark.do".equals(path)){
            deleteRemark(request,response);
        }else if("/workbench/activity/saveRemark.do".equals(path)){
            saveRemark(request,response);
        }else if("/workbench/activity/getRemarkContent.do".equals(path)){
            getRemarkContent(request,response);
        }else if("/workbench/activity/updateRemarkContent.do".equals(path)){
            updateRemarkContent(request,response);
        }
    }

    private void updateRemarkContent(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行修改备注操作");
        String id = request.getParameter("id");
        String noteContent = request.getParameter("newContent");
        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User)request.getSession().getAttribute("user")).getName();
        String editFlag = "1";
        ActivityRemark ar = new ActivityRemark();
        ar.setId(id);
        ar.setNoteContent(noteContent);
        ar.setEditTime(editTime);
        ar.setEditBy(editBy);
        ar.setEditFlag(editFlag);

        ActivityService service  = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = service.updateRemarkContent(ar);
        Map<String,Object> map = new HashMap<>();
        map.put("success",flag);
        map.put("ar",ar);
        PrintJson.printJsonObj(response,map);

    }

    private void getRemarkContent(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入修改备注信息模态窗口中获取备注的操作");
        String id = request.getParameter("id");
        ActivityService service  = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        String content = service.getRemarkContent(id);
        PrintJson.printJsonObj(response,content);
    }

    private void saveRemark(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行备注信息添加操作");
        String noteContent = request.getParameter("noteContent");
        String activityId = request.getParameter("activityId");
        String id = UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String editFlag = "0";

        ActivityRemark ar = new ActivityRemark();
        ar.setNoteContent(noteContent);
        ar.setActivityId(activityId);
        ar.setCreateBy(createBy);
        ar.setCreateTime(createTime);
        ar.setId(id);
        ar.setEditFlag(editFlag);

        ActivityService service  = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = service.saveRemark(ar);

        Map<String,Object> map = new HashMap<>();
        map.put("success",flag);
        map.put("ar",ar);
        PrintJson.printJsonObj(response,map);
    }

    private void deleteRemark(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("删除备注信息");
        String id = request.getParameter("id");
        ActivityService service = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = service.deleteRemark(id);
        PrintJson.printJsonFlag(response,flag);
    }

    private void getRemarkList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到显示备注信息列表操作");
        String activityId = request.getParameter("activityId");
        ActivityService service = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<ActivityRemark> arList = service.getRemarkList(activityId);
        PrintJson.printJsonObj(response,arList);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("跳转到活动的详细信息页");
        String id = request.getParameter("id");
        ActivityService service = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Activity a = service.detail(id);
        request.setAttribute("a",a);
        request.getRequestDispatcher("/workbench/activity/detail.jsp").forward(request,response);
    }

    private void update(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行市场活动信息修改操作");

        // 接收请求参数
        String id = request.getParameter("id");
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");
        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User)request.getSession().getAttribute("user")).getName();

        // 将参数封装成Activity对象
        Activity activity = new Activity();
        activity.setCost(cost);
        activity.setEditTime(editTime);
        activity.setEditBy(editBy);
        activity.setDescription(description);
        activity.setId(id);
        activity.setName(name);
        activity.setOwner(owner);

        ActivityService service = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = service.update(activity);
        PrintJson.printJsonFlag(response,flag);

    }

    private void getUserListAndActicity(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到市场活动根据id获取信息的修改操作");
        String id = request.getParameter("id");
        System.out.println("id = " + id);
        ActivityService service  = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Map<String,Object> map = service.getUserListAndActicity(id);

        PrintJson.printJsonObj(response,map);

    }

    private void delete(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到市场活动删除操作");
        // 获取请求参数
        String[] ids = request.getParameterValues("id");
        ActivityService service  = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = service.delete(ids);
        PrintJson.printJsonFlag(response,flag);
    }

    private void pageList(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter("name");
        String owner = request.getParameter("owner");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        // 页数
        String pageNoStr = request.getParameter("pageNo");
        // 每页的记录数
        String pageSizeStr = request.getParameter("pageSize");
        Integer pageNo = Integer.valueOf(pageNoStr);
        Integer pageSize = Integer.valueOf(pageSizeStr);
        // 略过的记录数
        Integer skipCount = (pageNo - 1) * pageSize;

        // 将以上索要查询的信息封装为map集合
        Map<String,Object> map = new HashMap<>();
        map.put("name",name);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);

        // 业务层
        ActivityService service = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        PaginationVo vo =  service.pageList(map);
        PrintJson.printJsonObj(response,vo);

    }

    private void saveActivity(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行市场活动操作");
        // 接收请求参数
        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        System.out.println("createBy = " + createBy);

        // 将参数封装成Activity对象
        Activity activity = new Activity();
        activity.setCost(cost);
        activity.setCreateBy(createBy);
        activity.setCreateTime(createTime);
        activity.setDescription(description);
        activity.setEndDate(endDate);
        activity.setStartDate(startDate);
        activity.setId(id);
        activity.setName(name);
        activity.setOwner(owner);

        ActivityService service = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = service.saveActivity(activity);
        PrintJson.printJsonFlag(response,flag);

    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {
        UserService service = (UserService) ServiceFactory.getService(new UserServiceImpl());

        List<User> userList = service.getUserList();

        PrintJson.printJsonObj(response,userList);
    }
}
