package com.yetiwu.crm.workbench.dao;

import com.yetiwu.crm.workbench.domain.Clue;

import java.util.List;
import java.util.Map;

public interface ClueDao {


    int saveClue(Clue clue);

    List<Clue> getClue(Map<String, Object> map);

    int getTotalCount(Map<String, Object> map);

    Clue detail(String id);

    Clue getClueById(String clueId);

    int deleteClueById(String clueId);
}
