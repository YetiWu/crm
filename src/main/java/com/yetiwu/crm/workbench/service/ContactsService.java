package com.yetiwu.crm.workbench.service;

import com.yetiwu.crm.workbench.domain.Contacts;

import java.util.List;

public interface ContactsService {

    List<Contacts> getContactsNameList(String name);
}
