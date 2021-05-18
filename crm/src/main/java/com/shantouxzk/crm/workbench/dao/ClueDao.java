package com.shantouxzk.crm.workbench.dao;

import com.shantouxzk.crm.workbench.domain.Clue;
import com.shantouxzk.crm.workbench.domain.ClueActivityRelation;

public interface ClueDao {
    void save(Clue clue);

    Clue detail(String id);

    void unbund(String id);

    void bund(ClueActivityRelation car);
}
