package com.yetiwu.crm.workbench.dao;

import com.yetiwu.crm.workbench.domain.ActivityRemark;

import java.util.List;

public interface ActivityRemarkDao {
    int getCountByAids(String[] ids);

    int deleteByAids(String[] ids);

    List<ActivityRemark> getRemarkList(String activityId);

    int deleteRemarkById(String id);

    int saveRemark(ActivityRemark ar);

    String getRemarkContent(String id);

    int updateRemarkContent(ActivityRemark ar);
}
