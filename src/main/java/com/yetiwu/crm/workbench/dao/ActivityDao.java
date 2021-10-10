package com.yetiwu.crm.workbench.dao;

import com.yetiwu.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityDao {
    int saveActivity(Activity activity);

    List<Activity> getActivityByCondition(Map<String, Object> map);

    int getTotalCount(Map<String, Object> map);

    int delete(String[] ids);

    Activity getActivityById(String id);

    int update(Activity activity);

    Activity getDetailById(String id);
}
