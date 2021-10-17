package com.yetiwu.crm.workbench.service;

import com.yetiwu.crm.vo.PaginationVo;
import com.yetiwu.crm.workbench.domain.Tran;
import com.yetiwu.crm.workbench.domain.TranHistory;

import java.util.List;
import java.util.Map;

public interface TransactionService {

    boolean save(Tran tran, String customerName);


    PaginationVo pageList(Map<String, Object> map);

    Tran detail(String id);

    List<TranHistory> getHistoryByTranId(String id);

    boolean changeStage(Tran tran);

    Map<String, Object> getCharts();
}
