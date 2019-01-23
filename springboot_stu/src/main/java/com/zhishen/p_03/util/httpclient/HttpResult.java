package com.zhishen.p_03.util.httpclient;

public class HttpResult {
    private int code;
    private String content;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public HttpResult() {
    }

    public HttpResult(int code, String content) {
        this.code = code;
        this.content = content;
    }

}
