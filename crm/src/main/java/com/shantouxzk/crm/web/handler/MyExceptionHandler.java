package com.shantouxzk.crm.web.handler;

import com.shantouxzk.crm.exception.LoginException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class MyExceptionHandler {
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

    @ExceptionHandler
    @ResponseBody
    public Map<String,Object> doDefaultException(Exception e){
        e.printStackTrace();
        Map<String,Object> map = new HashMap<>();
        map.put("success",false);
        return map;
    }
}
