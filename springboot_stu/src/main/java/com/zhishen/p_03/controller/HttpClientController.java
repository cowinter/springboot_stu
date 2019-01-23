package com.zhishen.p_03.controller;

import com.zhishen.p_03.util.httpclient.HttpResult;
import com.zhishen.p_03.util.httpclient.SingleHttpConnManager;
import org.apache.juli.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/httpclient")
public class HttpClientController {
    private Logger logger = LoggerFactory.getLogger(HttpClientController.class);
    @RequestMapping(value="/doPost",method = RequestMethod.POST)
    public HttpResult httpTest(){
        logger.info("httpTest is running");
        String url = "http://localhost:8080/p/httpclient/postTest";
        Map<String,String> header = new HashMap<>();
        header.put("Cookie", "123");
        header.put("Connection", "keep-alive");
        header.put("Accept", "application/json");
        header.put("Accept-Language", "zh-CN,zh;q=0.9");
        header.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
        Map<String,String> params = new HashMap<>();
        params.put("code","111");
        params.put("message","congratulations");
        HttpResult httpResult = SingleHttpConnManager.doPost(url,header,params);
        return httpResult;
    }

    @RequestMapping(value="/postTest",method=RequestMethod.POST)
    public String postTest(@RequestParam String code,@RequestParam String message){
        //@RequestParam String code, @RequestParam String message
        logger.info("postTest is running-->"+code+"__"+message);
        return "POST SUCCESS-->"+code+"_"+message;
    }
}
