package com.zhishen.p_03.service;

import com.zhishen.p_03.entity.Blog;
import org.springframework.stereotype.Service;

import java.util.List;
//@Service
public interface BlogService {
    public List<Blog> getBlogList() throws Exception;

    public Blog getBlogById(Long id) throws Exception;
}
