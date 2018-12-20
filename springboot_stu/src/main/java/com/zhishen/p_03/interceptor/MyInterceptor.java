package com.zhishen.p_03.interceptor;

import com.zhishen.p_03.entity.User;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyInterceptor implements HandlerInterceptor{

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
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
