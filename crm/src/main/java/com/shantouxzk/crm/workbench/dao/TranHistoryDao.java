package com.shantouxzk.crm.workbench.dao;

import com.shantouxzk.crm.workbench.domain.TranHistory;

import java.util.List;

public interface TranHistoryDao {
    int save(TranHistory tranHistory);

    List<TranHistory> getHistoryListByTranId(String tranId);
}
