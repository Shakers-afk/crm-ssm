package com.shantouxzk.crm.workbench.dao;

import com.shantouxzk.crm.workbench.domain.ClueActivityRelation;

import java.util.List;

public interface ClueActivityRelationDao {
    List<ClueActivityRelation> getListByClueId(String clueId);

    int unbund(String id);

    int bund(ClueActivityRelation car);

    int delete(ClueActivityRelation clueActivityRelation);
}
