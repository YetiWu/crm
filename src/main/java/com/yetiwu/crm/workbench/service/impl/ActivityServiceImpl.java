package com.yetiwu.crm.workbench.service.impl;

import com.yetiwu.crm.settings.dao.UserDao;
import com.yetiwu.crm.settings.domain.User;
import com.yetiwu.crm.utils.SqlSessionUtil;
import com.yetiwu.crm.vo.PaginationVo;
import com.yetiwu.crm.workbench.dao.ActivityDao;
import com.yetiwu.crm.workbench.dao.ActivityRemarkDao;
import com.yetiwu.crm.workbench.domain.Activity;
import com.yetiwu.crm.workbench.domain.ActivityRemark;
import com.yetiwu.crm.workbench.service.ActivityService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityServiceImpl implements ActivityService {
    private ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
    private ActivityRemarkDao activityRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    @Override
    public boolean saveActivity(Activity activity) {
        boolean flag = true;
        int count = activityDao.saveActivity(activity);
        if(count != 1){
            flag = false;
        }

        return flag;
    }

    @Override
    public PaginationVo pageList(Map<String, Object> map) {
        // 查询市场信息列表
        List<Activity> pageList = activityDao.getActivityByCondition(map);
        // 查询总记录数
        int totalCount = activityDao.getTotalCount(map);

        PaginationVo vo = new PaginationVo();
        vo.setPageList(pageList);
        vo.setTotal(totalCount);

        return vo;
    }

    @Override
    public boolean delete(String[] ids) {
        boolean flag = true;
        // 先删除备注信息
        // 查询要删除的备注信息的条数
        int count1 = activityRemarkDao.getCountByAids(ids);
        // 删除备注信息的条数
        int count2 = activityRemarkDao.deleteByAids(ids);
        if(count1 != count2){
            flag = false;
        }
        // 再删除市场活动信息
        int delCount = activityDao.delete(ids);
        if(delCount != ids.length){
            flag = false;
        }
        return flag;
    }

    @Override
    public Map<String, Object> getUserListAndActicity(String id) {
        Map<String,Object> map = new HashMap<>();
        // 获取用户信息
        List<User> uList = userDao.getUserList();

        // 获取对应的市场活动信息
        Activity activity = activityDao.getActivityById(id);
        map.put("uList",uList);
        map.put("a",activity);

        return map;
    }

    @Override
    public boolean update(Activity activity) {
        boolean flag = true;
        int count = activityDao.update(activity);
        if(count != 1){
            flag = false;
        }

        return flag;
    }

    @Override
    public Activity detail(String id) {
        Activity a = activityDao.getDetailById(id);
        return a;
    }

    @Override
    public List<ActivityRemark> getRemarkList(String activityId) {
        List<ActivityRemark> arList = activityRemarkDao.getRemarkList(activityId);
        return arList;
    }

    @Override
    public boolean deleteRemark(String id) {
        boolean flag = true;
        int count = activityRemarkDao.deleteRemarkById(id);
        if(count != 1){
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean saveRemark(ActivityRemark ar) {
        boolean flag = true;
        int count = activityRemarkDao.saveRemark(ar);
        if(count != 1){
            flag = false;
        }
        return flag;
    }

    @Override
    public String getRemarkContent(String id) {
        String content = activityRemarkDao.getRemarkContent(id);
        return content;
    }

    @Override
    public boolean updateRemarkContent(ActivityRemark ar) {
        boolean flag = true;
        int count = activityRemarkDao.updateRemarkContent(ar);
        if(count != 1){
            flag = false;
        }
        return flag;
    }
}
