package com.yetiwu.crm.workbench.service.impl;

import com.yetiwu.crm.utils.DateTimeUtil;
import com.yetiwu.crm.utils.SqlSessionUtil;
import com.yetiwu.crm.utils.UUIDUtil;
import com.yetiwu.crm.vo.PaginationVo;
import com.yetiwu.crm.workbench.dao.CustomerDao;
import com.yetiwu.crm.workbench.dao.TranDao;
import com.yetiwu.crm.workbench.dao.TranHistoryDao;
import com.yetiwu.crm.workbench.domain.Customer;
import com.yetiwu.crm.workbench.domain.Tran;
import com.yetiwu.crm.workbench.domain.TranHistory;
import com.yetiwu.crm.workbench.service.TransactionService;
import com.yetiwu.crm.workbench.vo.Pagination;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionServiceImpl implements TransactionService {
    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);

    @Override
    public boolean save(Tran tran, String customerName) {
        boolean flag = true;
        // 根据客户名称来精确查询客户信息
        Customer customer = customerDao.getCustomerByName(customerName);
        if(customer == null){
            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setCreateTime(DateTimeUtil.getSysTime());
            customer.setCreateBy(tran.getCreateBy());
            customer.setContactSummary(tran.getContactSummary());
            customer.setNextContactTime(tran.getNextContactTime());
            int count = customerDao.save(customer);
            if(count != 1){
                flag = false;
            }
        }

        // 将交易中的客户id补充
        tran.setCustomerId(customer.getId());
        // 添加交易
        int count1 = tranDao.save(tran);
        if(count1 != 1){
            flag = false;
        }

        // 添加交易历史
        TranHistory tranHistory = new TranHistory();
        tranHistory.setId(UUIDUtil.getUUID());
        tranHistory.setTranId(tran.getId());
        tranHistory.setExpectedDate(tran.getExpectedDate());
        tranHistory.setStage(tran.getStage());
        tranHistory.setMoney(tran.getMoney());
        tranHistory.setCreateBy(tran.getCreateBy());
        tranHistory.setCreateTime(tran.getCreateTime());
        int count2 = tranHistoryDao.save(tranHistory);
        if(count2 != 1){
            flag = false;
        }

        return flag;
    }

    @Override
    public PaginationVo pageList(Map<String,Object> map) {
        List<Tran> tran = tranDao.pageList(map);
        int total = tranDao.getTotal(map);
        PaginationVo vo = new PaginationVo();
        vo.setPageList(tran);
        vo.setTotal(total);
        return vo;
    }

    @Override
    public Tran detail(String id) {
        Tran tran = tranDao.detail(id);
        return tran;
    }

    @Override
    public List<TranHistory> getHistoryByTranId(String id) {
        List<TranHistory> tranHistoryList = tranHistoryDao.getHistoryByTranId(id);
        return tranHistoryList;
    }

    @Override
    public boolean changeStage(Tran tran) {
        boolean flag = true;
        // 改变交易阶段，并修改详情信息
        int count = tranDao.changeStage(tran);
        if(count != 1){
            flag = false;
        }

        // 添加新的交易历史
        TranHistory th = new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setPossibility(tran.getPossibility());
        th.setStage(tran.getStage());
        th.setMoney(tran.getMoney());
        th.setTranId(tran.getId());
        th.setCreateTime(tran.getEditTime());
        th.setCreateBy(tran.getEditBy());
        th.setExpectedDate(tran.getExpectedDate());
        int count1 = tranHistoryDao.save(th);
        if(count1 != 1){
            flag = false;
        }
        return flag;
    }

    @Override
    public Map<String, Object> getCharts() {
        List<Map<Integer,String>> dataList = tranDao.getGroupByStage();
        int total = tranDao.getAll();
        Map<String,Object> map = new HashMap<>();
        map.put("total",total);
        map.put("dataList",dataList);
        return map;
    }
}
