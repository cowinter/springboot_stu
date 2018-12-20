package com.zhishen.p_03.config;

import com.zhishen.p_03.interceptor.MyInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class MyMvcConfig extends WebMvcConfigurationSupport {
    private Logger logger = LoggerFactory.getLogger(MyMvcConfig.class);
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        logger.info("static res set");
        registry.addResourceHandler("/resource/**").addResourceLocations("classpath:/static/picture/");
        super.addResourceHandlers(registry);
    }

    @Override
    protected void addViewControllers(ViewControllerRegistry registry) {
        logger.info("default view");
        registry.addViewController("/toIndex").setViewName("/index");
        super.addViewControllers(registry);
    }

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        logger.info("Not logged in or Log out of date");
        //registry.addInterceptor(new MyInterceptor()).addPathPatterns("/**").excludePathPatterns("/toIndex");
        super.addInterceptors(registry);
    }
}
