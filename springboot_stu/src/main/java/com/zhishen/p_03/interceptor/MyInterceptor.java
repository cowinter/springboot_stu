package com.zhishen.p_03.interceptor;

import com.zhishen.p_03.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyInterceptor implements HandlerInterceptor{

    private Logger logger = LoggerFactory.getLogger(MyInterceptor.class);
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("MyInterceptor: judge if is invoked");
        boolean flag = true;
        User user = (User)request.getSession().getAttribute("user");
        if(user == null){
            flag = false;
            response.sendRedirect("/toIndex");
            return flag;
        }
        return flag;
    }

}
