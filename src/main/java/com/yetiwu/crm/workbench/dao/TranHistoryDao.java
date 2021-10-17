package com.yetiwu.crm.workbench.dao;

import com.yetiwu.crm.workbench.domain.TranHistory;

import java.util.List;

public interface TranHistoryDao {

    int save(TranHistory tranHistory);

    List<TranHistory> getHistoryByTranId(String id);
}
