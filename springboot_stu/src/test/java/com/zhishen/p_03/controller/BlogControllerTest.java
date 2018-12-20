package com.zhishen.p_03.controller;

import com.zhishen.p_03.entity.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogControllerTest {
    @Autowired
    private WebApplicationContext applicationContext;
    private MockMvc mockMvc;
    private MockHttpSession mockHttpSession;
    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext).build();
        mockHttpSession = new MockHttpSession();
        User user = new User(1,"大大");
        mockHttpSession.setAttribute("user",user);
    }

    @Test
    public void blogList() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/blog/list")
                            .contentType(MediaType.APPLICATION_JSON_UTF8)
                            .accept(MediaType.APPLICATION_JSON_UTF8)
                            .session(mockHttpSession))
                .andExpect(MockMvcResultMatchers.status().isOk())
                //.andExpect(MockMvcResultMatchers.jsonPath("$.author").value("11"))
                //.andExpect(MockMvcResultMatchers.jsonPath("$.title").value("22"))
                //.andReturn();
                .andDo(MockMvcResultHandlers.print());
    }

}