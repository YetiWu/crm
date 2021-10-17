package com.yetiwu.crm.workbench.dao;

import com.yetiwu.crm.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkDao {

    List<ClueRemark> getClueRemarkByClueId(String clueId);

    int deleteRemarkByClueId(String clueId);
}
