package com.shantouxzk.crm.web.listener;

import com.shantouxzk.crm.settings.domain.DicValue;
import com.shantouxzk.crm.settings.service.DicService;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;

public class SysInitListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        //数据字典
        ServletContext application = sce.getServletContext();
        WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(application);
        DicService dicService = ctx.getBean(DicService.class);;

        Map<String, List<DicValue>> map = dicService.getAll();
        Set<String> keySet = map.keySet();
        keySet.forEach(key->application.setAttribute(key,map.get(key)));

        //解析properties
        Map<String,String> pMap = new HashMap<>();
        ResourceBundle bundle = ResourceBundle.getBundle("Stage2Possibility");
        Enumeration<String> bundleKeys = bundle.getKeys();
        while (bundleKeys.hasMoreElements()){
            String key = bundleKeys.nextElement();
            String value = bundle.getString(key);
            pMap.put(key,value);
        }
        application.setAttribute("pMap",pMap);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
