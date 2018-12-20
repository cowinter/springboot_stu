package com.zhishen.p_03.service;

import com.zhishen.p_03.entity.Blog;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogServiceTest {
    @Autowired
    private BlogService blogService;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void getBlogList() throws Exception {
        List<Blog> blogList = blogService.getBlogList();
        Assert.assertThat(blogList.get(0).getAuthor(),equalTo("官方SpriongBoot例子"));
        //System.out.print(blogList.size());
    }

}