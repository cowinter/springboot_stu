package com.zhishen.p_03.service.impl;

import com.zhishen.p_03.dao.BlogDao;
import com.zhishen.p_03.entity.Blog;
import com.zhishen.p_03.mapper.BlogMapper;
import com.zhishen.p_03.service.BlogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
@Service
public class BlogServiceImpl implements BlogService{
    private Logger logger = LoggerFactory.getLogger(BlogServiceImpl.class);
//    @Autowired
//    private BlogDao blogDao;
//    @Override
//    public List<Blog> getBlogList() {
//        List<Blog> blogList = null;
//        blogList = blogDao.getBlogList();
//        return blogList;
//    }
      @Autowired
      private BlogMapper blogMapper;

      public List<Blog> getBlogList() {
        logger.debug("BlogServiceImpl is running");
        List<Blog> blogList = null;
        try {
            blogList = blogMapper.getBlogs();
        } catch (Exception e){
            e.printStackTrace();
            logger.info("blogMapper error");
        }
        return blogList;
      }
}
