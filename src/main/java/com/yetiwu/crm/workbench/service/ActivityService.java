package com.yetiwu.crm.workbench.service;

import com.yetiwu.crm.vo.PaginationVo;
import com.yetiwu.crm.workbench.domain.Activity;
import com.yetiwu.crm.workbench.domain.ActivityRemark;

import java.util.List;
import java.util.Map;

public interface ActivityService {
    boolean saveActivity(Activity activity);

    PaginationVo pageList(Map<String, Object> map);

    boolean delete(String[] ids);

    Map<String, Object> getUserListAndActicity(String id);

    boolean update(Activity activity);

    Activity detail(String id);

    List<ActivityRemark> getRemarkList(String activityId);

    boolean deleteRemark(String id);

    boolean saveRemark(ActivityRemark ar);

    String getRemarkContent(String id);

    boolean updateRemarkContent(ActivityRemark ar);
}
