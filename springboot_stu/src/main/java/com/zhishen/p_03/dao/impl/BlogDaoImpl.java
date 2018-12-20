package com.zhishen.p_03.dao.impl;

import com.zhishen.p_03.dao.BlogDao;
import com.zhishen.p_03.entity.Blog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class BlogDaoImpl implements BlogDao{
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Override
    public List<Blog> getBlogList() {
        List<Blog> bloglist = null;
        String sql = "SELECT * FROM BLOG";
//        bloglist = jdbcTemplate.queryForList(sql,Blog.class);
//        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql);
        List<Map<String,Object>> blogResult = jdbcTemplate.queryForList(sql);
        bloglist = Blog.toObjectList(blogResult);
        return bloglist;
    }
}
