package com.shantouxzk.crm.workbench.service.impl;

import com.shantouxzk.crm.utils.UUIDUtil;
import com.shantouxzk.crm.workbench.dao.ClueDao;
import com.shantouxzk.crm.workbench.domain.Clue;
import com.shantouxzk.crm.workbench.domain.ClueActivityRelation;
import com.shantouxzk.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClueServiceImpl implements ClueService {
    private ClueDao clueDao;

    @Autowired
    public void setClueDao(ClueDao clueDao){
        this.clueDao = clueDao;
    }

    @Override
    public Boolean save(Clue clue) {
        clueDao.save(clue);
        return true;
    }

    @Override
    public Clue detail(String id) {
        return clueDao.detail(id);
    }

    @Override
    public Boolean unbund(String id) {
        clueDao.unbund(id);
        return true;
    }

    @Transactional
    @Override
    public Boolean bund(String clueId, String[] activityIds) {
        ClueActivityRelation car = null;
        for (String activityId:activityIds){
            car = new ClueActivityRelation();
            String id = UUIDUtil.getUUID();
            car.setId(id);
            car.setActivityId(activityId);
            car.setClueId(clueId);

            clueDao.bund(car);
        }
        return true;
    }
}
