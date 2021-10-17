package com.yetiwu.crm.web.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yetiwu.crm.settings.domain.DicType;
import com.yetiwu.crm.settings.domain.DicValue;
import com.yetiwu.crm.utils.PrintJson;
import com.yetiwu.crm.utils.ServiceFactory;
import com.yetiwu.crm.workbench.service.ClueService;
import com.yetiwu.crm.workbench.service.impl.ClueServiceImpl;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.util.*;

// 全局作用域监听器
public class SysInitListener implements ServletContextListener{
    // 在application被创建的时候执行
    @Override
    public void contextInitialized(ServletContextEvent event) {
        System.out.println("数据字典初始化开始");
        // 获取全局作用域对象
        ServletContext sc = event.getServletContext();

        // 调用业务层的方法来获取数据字典的内容
        ClueService service = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        Map<String, List<DicValue>> map = service.getAll();

        for(String key : map.keySet()){
            sc.setAttribute(key,map.get(key));
        }
        System.out.println("数据字典初始化结束");

        // 将交易阶段和可能性的键值对关系保存在数据字典中
        Map<String,String> pMap = new HashMap<>();
        // 1、解析properties文件
        ResourceBundle bundle = ResourceBundle.getBundle("Stage2Possibility");
        // 2、获取properties文件中的key（stage）集合
        Enumeration<String> stages = bundle.getKeys();
        // 3、遍历枚举类来取key
        while(stages.hasMoreElements()){
            String stage = stages.nextElement();
            String possbility = bundle.getString(stage);
            pMap.put(stage,possbility);
        }
        sc.setAttribute("pMap",pMap);

    }
}
