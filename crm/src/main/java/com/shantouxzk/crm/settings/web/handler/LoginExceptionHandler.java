package com.shantouxzk.crm.settings.web.handler;

import com.shantouxzk.crm.settings.exception.LoginException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class LoginExceptionHandler{
    @ExceptionHandler(LoginException.class)
    @ResponseBody
    public Map<String,Object> doLoginException(Exception e){
//        e.printStackTrace();
        Map<String,Object> map = new HashMap<>();
        String msg = e.getMessage();
        map.put("msg",msg);
        map.put("success",false);

        return map;
    }

//    @ExceptionHandler(LoginException.class)
//    public void doLoginException(HttpServletResponse response, Exception e){
//        e.printStackTrace();
//        Map<String,Object> map = new HashMap<>();
//        String msg = e.getMessage();
//        map.put("msg",msg);
//        map.put("success",false);
//
//        PrintJson.printJsonObj(response,map);
//    }

//    @ExceptionHandler
//    public void doDefaultException(Exception e){
//        e.printStackTrace();
//    }
}
