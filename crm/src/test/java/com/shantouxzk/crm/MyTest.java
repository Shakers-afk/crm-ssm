package com.shantouxzk.crm;

import com.shantouxzk.crm.utils.DateTimeUtil;
import org.junit.Test;

public class MyTest {
    @Test
    public void test01() {
        String myDate = "2021-08-28 08:00:00";

        String currentDate = DateTimeUtil.getSysTime();
        int count = currentDate.compareTo(myDate);
        System.out.println(count);
    }
}
