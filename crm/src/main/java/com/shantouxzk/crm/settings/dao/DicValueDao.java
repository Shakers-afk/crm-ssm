package com.shantouxzk.crm.settings.dao;

import com.shantouxzk.crm.settings.domain.DicValue;

import java.util.List;

public interface DicValueDao {
    List<DicValue> getListByCode(String code);
}
