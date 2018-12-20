package com.zhishen.p_03.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Blog {
    private long id;
    private String author;
    private String title;
    private String url;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public static Blog toObject(Map<String, Object> map) {
        Blog blog = new Blog();
        blog.setId((Long)map.get("id"));
        blog.setAuthor((String)map.get("author"));
        blog.setTitle((String)map.get("title"));
        blog.setUrl((String)map.get("url"));
        return blog;
    }

    public static List<Blog> toObjectList(List<Map<String,Object>> mapList){
        List<Blog> blogList = new ArrayList<>();
        for(Map<String,Object> map : mapList){
            blogList.add(toObject(map));
        }
        return blogList;
    }
}
