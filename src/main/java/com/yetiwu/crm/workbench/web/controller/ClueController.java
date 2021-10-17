package com.yetiwu.crm.workbench.web.controller;

import com.yetiwu.crm.settings.domain.User;
import com.yetiwu.crm.utils.DateTimeUtil;
import com.yetiwu.crm.utils.PrintJson;
import com.yetiwu.crm.utils.ServiceFactory;
import com.yetiwu.crm.utils.UUIDUtil;
import com.yetiwu.crm.vo.PaginationVo;
import com.yetiwu.crm.workbench.domain.Activity;
import com.yetiwu.crm.workbench.domain.Clue;
import com.yetiwu.crm.workbench.domain.Tran;
import com.yetiwu.crm.workbench.service.ActivityService;
import com.yetiwu.crm.workbench.service.ClueService;
import com.yetiwu.crm.workbench.service.impl.ActivityServiceImpl;
import com.yetiwu.crm.workbench.service.impl.ClueServiceImpl;
import javafx.beans.property.ReadOnlySetProperty;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClueController extends HttpServlet{

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到crm项目中");
        String path = request.getServletPath();
        if("/workbench/clue/getUserList.do".equals(path)){
            getUserList(request,response);
        }else if("/workbench/clue/saveClue.do".equals(path)){
            saveClue(request,response);
        }else if("/workbench/clue/pageList.do".equals(path)){
            pageList(request,response);
        }else if("/workbench/clue/detail.do".equals(path)){
            detail(request,response);
        }else if("/workbench/clue/getActivityListById.do".equals(path)){
            getActivityListById(request,response);
        }else if("/workbench/clue/unbund.do".equals(path)){
            unbund(request,response);
        }else if("/workbench/clue/getActivityList.do".equals(path)){
            getActivityList(request,response);
        }else if("/workbench/clue/bund.do".equals(path)){
            bund(request,response);
        }else if("/workbench/clue/getActivityListByName.do".equals(path)){
            openSearchModal(request,response);
        }else if("/workbench/clue/convert.do".equals(path)){
            convert(request,response);
        }
    }

    private void convert(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("转换市场活动");

        String clueId = request.getParameter("clueId");

        // 获取flag的值，若为a则为表单提交方式，意为创建了交易
        String flag = request.getParameter("flag");
        // 声明交易实体类
        Tran tran = null;
        // 从session域中获取创建者
        String createBy = ((User)request.getSession().getAttribute("user")).getName();

        if("a".equals(flag)){
            // 走到这里说明创建了一笔交易
            // 获取交易提交的参数
            String money = request.getParameter("money");
            String name = request.getParameter("name");
            String expectedDate = request.getParameter("expectedDate");
            String stage = request.getParameter("stage");
            String activityId = request.getParameter("activityId");

            // 初始化交易实体类
            tran = new Tran();
            tran.setExpectedDate(expectedDate);
            tran.setActivityId(activityId);
            tran.setMoney(money);
            tran.setName(name);
            tran.setStage(stage);
        }
        // 调用业务层处理业务
        ClueService service = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
            /*
                参数：
                    clueId：线索id
                    tran对象：交易对象（根据该对象是否为空，来告诉业务层是否创建交易）
                    createBy：创建人
             */
        boolean flag1 = service.convert(clueId,tran,createBy);
        System.out.println("boolean = " + flag1);

        // 重定向到线索列表页面
        if(flag1){
            response.sendRedirect(request.getContextPath() + "/workbench/clue/index.jsp");
        }
    }

    private void openSearchModal(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("显示市场活动");
        String name = request.getParameter("name");
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<Activity> resList = as.getActivityListByName(name);
        PrintJson.printJsonObj(response,resList);
    }

    private void bund(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("关联市场活动");
        String cid = request.getParameter("clueId");
        String aid[] = request.getParameterValues("activityId");

        Map<String,Object> map = new HashMap<>();
        map.put("cid",cid);
        map.put("aid",aid);

        ClueService service = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag = service.bund(map);
        PrintJson.printJsonFlag(response,flag);

    }

    private void getActivityList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("查询为关联的市场活动信息列表");
        String name = request.getParameter("name");
        String clueId = request.getParameter("clueId");

        Map<String,String> map = new HashMap<>();
        map.put("name",name);
        map.put("clueId",clueId);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<Activity> resList = as.getActivityList(map);
        PrintJson.printJsonObj(response,resList);
    }

    private void unbund(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("解除关联市场活动");
        String id = request.getParameter("id");
        ClueService service = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag = service.unbund(id);
        PrintJson.printJsonFlag(response,flag);
    }

    private void getActivityListById(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("显示关联的市场活动列表");
        String clueId = request.getParameter("clueId");
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<Activity> aList = as.getActivityListById(clueId);
        PrintJson.printJsonObj(response,aList);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到信息跳转页面");
        String id = request.getParameter("id");
        ClueService service = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        Clue clue = service.detail(id);
        request.setAttribute("c",clue);
        request.getRequestDispatcher("/workbench/clue/detail.jsp").forward(request,response);
    }

    private void pageList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("展现线索列表信息");
        String pageNoStr = request.getParameter("pageNo");
        String pageSizeStr = request.getParameter("pageSize");
        String fullname = request.getParameter("fullname");
        String company = request.getParameter("company");
        String phone = request.getParameter("phone");
        String source = request.getParameter("source");
        String owner = request.getParameter("owner");
        String mphone = request.getParameter("mphone");
        String state = request.getParameter("state");

        System.out.println("pageNo = " + pageNoStr);
        System.out.println("pageSize = " + pageSizeStr);
        System.out.println("fullname = " + fullname);
        System.out.println("company = " + company);
        System.out.println("phone = " + phone);
        System.out.println("source = " + source);
        System.out.println("mphone = " + mphone);
        System.out.println("owner = " + owner);
        System.out.println("state = " + state);
        // 计算略过的记录数
        Integer pageNo = Integer.valueOf(pageNoStr);
        Integer pageSize = Integer.valueOf(pageSizeStr);
        Integer skipCount = (pageNo - 1) * pageSize;

        Map<String,Object> map = new HashMap<>();
        map.put("fullname",fullname);
        map.put("company",company);
        map.put("phone",phone);
        map.put("source",source);
        map.put("owner",owner);
        map.put("mphone",mphone);
        map.put("state",state);
        map.put("pageSize",pageSize);
        map.put("skipCount",skipCount);

        ClueService service = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        PaginationVo vo = service.pageList(map);
        PrintJson.printJsonObj(response,vo);
    }

    private void saveClue(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到添加线索操作中");
        // 获取请求参数
        String id = UUIDUtil.getUUID();
        String fullname = request.getParameter("fullname");
        String appellation = request.getParameter("appellation");
        String owner = request.getParameter("owner");
        String company = request.getParameter("company");
        String job = request.getParameter("job");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String website = request.getParameter("website");
        String mphone = request.getParameter("mphone");
        String state = request.getParameter("state");
        String source = request.getParameter("source");
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String createTime = DateTimeUtil.getSysTime();
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");
        String address = request.getParameter("address");

        System.out.println("owner = " + owner);
        System.out.println("appellation = " + appellation);
        System.out.println("state = " + state);
        System.out.println("source = " + source);

        Clue clue = new Clue();
        clue.setMphone(mphone);
        clue.setState(state);
        clue.setSource(source);
        clue.setCreateTime(createTime);
        clue.setNextContactTime(nextContactTime);
        clue.setAddress(address);
        clue.setAppellation(appellation);
        clue.setCompany(company);
        clue.setContactSummary(contactSummary);
        clue.setDescription(description);
        clue.setCreateBy(createBy);
        clue.setEmail(email);
        clue.setId(id);
        clue.setFullname(fullname);
        clue.setOwner(owner);
        clue.setPhone(phone);
        clue.setJob(job);
        clue.setWebsite(website);

        ClueService service = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag = service.saveClue(clue);
        PrintJson.printJsonFlag(response,flag);
    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到获得所有者信息操作");
        ClueService service = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        List<User> uList = service.getUserList();
        PrintJson.printJsonObj(response,uList);
    }
}
