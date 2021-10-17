package com.yetiwu.crm.workbench.service.impl;

import com.yetiwu.crm.utils.SqlSessionUtil;
import com.yetiwu.crm.workbench.dao.ContactsDao;
import com.yetiwu.crm.workbench.domain.Contacts;
import com.yetiwu.crm.workbench.service.ContactsService;

import java.util.List;

public class ContactsServiceImpl implements ContactsService {
    private ContactsDao contactsDao = SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);
    @Override
    public List<Contacts> getContactsNameList(String name) {
        List<Contacts> contactsList = contactsDao.getContactsNameList(name);
        return contactsList;
    }
}
