package com.zhishen.p_03.controller;

import com.zhishen.p_03.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@RestController
//@Controller
@RequestMapping("/index")
public class HelloController {
    private Logger logger =LoggerFactory.getLogger(HelloController.class);
    @RequestMapping("/hello")
    public ModelAndView index(){
        logger.info("index() run");
        List<User> userList = new ArrayList<>();
        userList.add(new User(1,"李一"));
        userList.add(new User(2,"王一二"));
        userList.add(new User(3,"张三哈"));
        ModelAndView mav = new ModelAndView();
        mav.setViewName("index");
        mav.addObject("userList",userList);
        return mav;
    }
    @RequestMapping("/log")
    public String logInfo(){
        logger.info("logInfo run");
        logger.trace("this is trace");
        logger.debug("this is debug");
        logger.info("this is info");
        logger.warn("this is warn");
        logger.error("this is error");

        return "Hello";
    }
}
