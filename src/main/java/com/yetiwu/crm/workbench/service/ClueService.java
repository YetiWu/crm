package com.yetiwu.crm.workbench.service;

import com.yetiwu.crm.settings.domain.DicType;
import com.yetiwu.crm.settings.domain.DicValue;
import com.yetiwu.crm.settings.domain.User;
import com.yetiwu.crm.vo.PaginationVo;
import com.yetiwu.crm.workbench.domain.Clue;
import com.yetiwu.crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface ClueService {
    Map<String, List<DicValue>> getAll();

    List<User> getUserList();

    boolean saveClue(Clue clue);

    PaginationVo pageList(Map<String,Object> map);

    Clue detail(String id);

    boolean unbund(String id);

    boolean bund(Map<String,Object> map);

    boolean convert(String clueId, Tran tran, String createBy);
}
