package com.yetiwu.crm.workbench.service.impl;

import com.yetiwu.crm.utils.SqlSessionUtil;
import com.yetiwu.crm.workbench.dao.CustomerDao;
import com.yetiwu.crm.workbench.domain.Customer;
import com.yetiwu.crm.workbench.service.CustomerService;

import java.util.List;

public class CustomerServiceImpl implements CustomerService {
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    @Override
    public List<String> getCustomerName(String name) {
        List<String> customerName = customerDao.getCustomerName(name);
        return customerName;
    }

}
