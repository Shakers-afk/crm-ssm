package com.shantouxzk.crm.workbench.service;

import com.shantouxzk.crm.workbench.domain.Tran;
import com.shantouxzk.crm.workbench.domain.TranHistory;

import java.util.List;
import java.util.Map;

public interface TranService {
    Boolean save(Tran tran, String customerName);

    Tran detail(String id);

    List<TranHistory> getHistoryListByTranId(String tranId);

    Boolean changeStage(Tran tran);

    Map<String, Object> getCharts();
}
