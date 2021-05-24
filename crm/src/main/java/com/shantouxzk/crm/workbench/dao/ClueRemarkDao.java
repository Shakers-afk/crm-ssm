package com.shantouxzk.crm.workbench.dao;

import com.shantouxzk.crm.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkDao {
    List<ClueRemark> getListByClueId(String clueId);

    int delete(ClueRemark clueRemark);
}
