package com.shantouxzk.crm;

import com.shantouxzk.crm.settings.domain.DicValue;
import com.shantouxzk.crm.settings.service.impl.DicServiceImpl;
import com.shantouxzk.crm.utils.DateTimeUtil;
import org.junit.Test;
import org.springframework.web.bind.annotation.RequestMapping;

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
        Boolean flag = true;
        if (flag!=null&&flag){
            System.out.println("flag:"+flag);
        }
    }

    @Test
    public void test03(){
        boolean flag;

        flag = false && false;
        System.out.println(flag);
    }
}
