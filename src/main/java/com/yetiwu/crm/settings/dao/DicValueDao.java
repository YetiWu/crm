package com.yetiwu.crm.settings.dao;

import com.yetiwu.crm.settings.domain.DicValue;

import java.util.List;

public interface DicValueDao {
    List<DicValue> getValueByCode(String code);
}
