package com.zhishen.p_03.mapper;

import com.zhishen.p_03.entity.Blog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface BlogMapper {
    //获取所有blog
    List<Blog> getBlogs() throws Exception;
}
