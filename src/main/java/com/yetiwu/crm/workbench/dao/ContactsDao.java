package com.yetiwu.crm.workbench.dao;

import com.yetiwu.crm.workbench.domain.Contacts;

import java.util.List;

public interface ContactsDao {

    int save(Contacts contacts);

    List<Contacts> getContactsNameList(String name);
}
