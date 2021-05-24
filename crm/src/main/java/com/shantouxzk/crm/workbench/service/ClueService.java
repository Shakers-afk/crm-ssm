package com.shantouxzk.crm.workbench.service;

import com.shantouxzk.crm.workbench.domain.Clue;
import com.shantouxzk.crm.workbench.domain.Tran;

import java.util.List;

/**
 * @author Shakers
 */
public interface ClueService {
    Boolean save(Clue clue);

    Clue detail(String id);

    Boolean unbund(String id);

    Boolean bund(String clueId, String[] activityIds);


    Boolean convert(String clueId, Tran tran, String createBy);

//    List<Clue> pageList(Integer pageNo, Integer pageSize, Clue clue);
}
