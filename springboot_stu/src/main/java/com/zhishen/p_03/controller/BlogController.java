package com.zhishen.p_03.controller;

import com.zhishen.p_03.entity.Blog;
import com.zhishen.p_03.service.BlogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import java.util.List;


@RestController
@RequestMapping("/blog")
public class BlogController {
    private Logger logger = LoggerFactory.getLogger(BlogController.class);

    @Autowired
    private BlogService blogService;
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public ModelAndView blogList() throws Exception {
        logger.debug("BlogController: blogList is running");

        ModelAndView mav = new ModelAndView();
        List<Blog> blogList = null;
        blogList = blogService.getBlogList();
        mav.setViewName("/index");
        mav.addObject(blogList);
        return mav;
    }
	
	/**
     * 检查table
     * @return
     */
    @RequestMapping(value = "/blogCheck",method = RequestMethod.GET)
    public String CheckBlogExist(){
        logger.info("check blog database whether exsits");
        List<Blog> blogList = null;
        try {
            blogList = blogService.getBlogList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String message = blogList.size()>0 ? "Yes" : "No";
        return message;
    }
}
