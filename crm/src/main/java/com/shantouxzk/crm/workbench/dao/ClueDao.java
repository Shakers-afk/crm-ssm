package com.shantouxzk.crm.workbench.dao;

import com.shantouxzk.crm.workbench.domain.Activity;
import com.shantouxzk.crm.workbench.domain.Clue;

import java.util.List;

public interface ClueDao {
    int save(Clue clue);

    Clue detail(String id);

    Clue getById(String clueId);

    int delete(String clueId);

//    List<Clue> getClueListByCondition(Clue clue);
}
