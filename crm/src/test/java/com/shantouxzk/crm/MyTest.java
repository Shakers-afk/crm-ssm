package com.shantouxzk.crm;

import com.shantouxzk.crm.settings.domain.DicValue;
import com.shantouxzk.crm.settings.service.impl.DicServiceImpl;
import com.shantouxzk.crm.utils.DateTimeUtil;
import org.junit.Test;

import java.util.List;
import java.util.Map;


public class MyTest {
    @Test
    public void test01() {
        String myDate = "2021-08-28 08:00:00";

        String currentDate = DateTimeUtil.getSysTime();
        int count = currentDate.compareTo(myDate);
        System.out.println(count);
    }

    @Test
    public void test02(){
        Map<String, List<DicValue>> map = (new DicServiceImpl()).getAll();

    }
}
