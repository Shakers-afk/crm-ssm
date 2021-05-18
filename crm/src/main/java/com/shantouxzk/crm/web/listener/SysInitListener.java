package com.shantouxzk.crm.web.listener;

import com.shantouxzk.crm.settings.domain.DicValue;
import com.shantouxzk.crm.settings.service.DicService;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SysInitListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext application = sce.getServletContext();
        WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(application);
        DicService dicService = ctx.getBean(DicService.class);;

        Map<String, List<DicValue>> map = dicService.getAll();
        Set<String> keySet = map.keySet();

        keySet.forEach(key->application.setAttribute(key,map.get(key)));
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
