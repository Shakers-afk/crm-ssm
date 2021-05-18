package com.shantouxzk.crm.workbench.service;

import com.shantouxzk.crm.workbench.domain.Clue;

public interface ClueService {
    Boolean save(Clue clue);

    Clue detail(String id);

    Boolean unbund(String id);

    Boolean bund(String clueId, String[] activityIds);
}
