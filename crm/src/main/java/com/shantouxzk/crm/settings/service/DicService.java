package com.shantouxzk.crm.settings.service;

import com.shantouxzk.crm.settings.domain.DicValue;

import java.util.List;
import java.util.Map;

public interface DicService {
    Map<String, List<DicValue>> getAll();
}
