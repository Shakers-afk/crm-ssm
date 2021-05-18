package com.shantouxzk.crm.settings.service.impl;

import com.shantouxzk.crm.settings.dao.DicTypeDao;
import com.shantouxzk.crm.settings.dao.DicValueDao;
import com.shantouxzk.crm.settings.domain.DicType;
import com.shantouxzk.crm.settings.domain.DicValue;
import com.shantouxzk.crm.settings.service.DicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DicServiceImpl implements DicService {
    private DicTypeDao dicTypeDao;
    private DicValueDao dicValueDao;
    @Autowired
    public void setDicTypeDao(DicTypeDao dicTypeDao) {
        this.dicTypeDao = dicTypeDao;
    }
    @Autowired
    public void setDicValueDao(DicValueDao dicValueDao) {
        this.dicValueDao = dicValueDao;
    }

    @Override
    public Map<String, List<DicValue>> getAll() {
        Map<String, List<DicValue>> map = new HashMap<>();
        List<DicType> dtList = dicTypeDao.getTypeList();

        for(DicType dt : dtList){
            String code = dt.getCode();
            List<DicValue> dvList = dicValueDao.getListByCode(code);
            map.put(code+"List",dvList);
        }
        return map;
    }
}
