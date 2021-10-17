package com.yetiwu.crm.workbench.dao;

import com.yetiwu.crm.vo.PaginationVo;
import com.yetiwu.crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface TranDao {

    int save(Tran tran);

    List<Tran> pageList(Map<String, Object> map);

    int getTotal(Map<String, Object> map);

    Tran detail(String id);

    int changeStage(Tran tran);

    List<Map<Integer, String>> getGroupByStage();

    int getAll();
}
