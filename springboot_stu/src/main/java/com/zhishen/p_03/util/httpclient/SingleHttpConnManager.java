package com.zhishen.p_03.util.httpclient;

import ch.qos.logback.core.net.SyslogOutputStream;
import org.apache.http.*;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 单线程httpclient
 * HttpGet,HttpPost
 */
public class SingleHttpConnManager {
    private static PoolingHttpClientConnectionManager connectionManager;
    //public static void main(String[] args){
        /************************************************************
        //连接池管理
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        //最大连接数
        connectionManager.setMaxTotal(200);
        //每个路由的最大连接数
        connectionManager.setDefaultMaxPerRoute(20);

        String url = "http://www.baidu.com/s?wd=mvnrepository";
        long start = System.currentTimeMillis();
        for(int i=0;i<3;i++){
            doGet(connectionManager,url); 
        }
        long end = System.currentTimeMillis();
        System.out.print("3次GET请求耗时=="+(end-start));
        //清理无效连接
        new IdleConnectionEvictor(connectionManager).start();
        **************************************************************/
    //}

    /**
     * 获取连接池实例
     * @return
     */
    private static PoolingHttpClientConnectionManager getConnectionManager() {
        if(connectionManager == null){
            connectionManager = new PoolingHttpClientConnectionManager();
            connectionManager.setMaxTotal(200);
            connectionManager.setDefaultMaxPerRoute(20);
            return connectionManager;
        } else {
            return connectionManager;
        }
    }

    /**
     * RequestConfig
     * @param httpMethod
     */
    private static void defaultRequestConfig(HttpRequestBase httpMethod){
        //连接相关配置
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(1000)//创建连接的最长时间
                .setConnectionRequestTimeout(500) //连接池中获取连接的最长时间
                .setSocketTimeout(1000*10) //数据传输的最长时间
                .build();
        httpMethod.setConfig(requestConfig);
    }

    private static CloseableHttpClient defaultHttpClient(){
        //连接池中获取httpclient对象
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(getConnectionManager())
                .setRetryHandler(httpRequestRetryHandler(5))
                .build();
        return httpClient;
    }

    public static void doGet(PoolingHttpClientConnectionManager connectionManager, String url) {
        //连接池中获取httpclient对象
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setRetryHandler(httpRequestRetryHandler(5))
                .build();
        //GET请求对象
        HttpGet httpGet = new HttpGet(url);
        //请求配置
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(1000)//创建连接的最长时间
                .setConnectionRequestTimeout(500) //连接池中获取连接的最长时间
                .setSocketTimeout(1000*10) //数据传输的最长时间
                .build();
        httpGet.setConfig(requestConfig);
        CloseableHttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(httpGet);
            if(httpResponse.getStatusLine().getStatusCode() == 200){
                String context = EntityUtils.toString(httpResponse.getEntity(),"UTF-8");
                System.out.println("context长度=="+context.length());
                System.out.print(context);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * HttpPost请求
     * @param url
     * @param header
     * @param params
     */
    public static HttpResult doPost(String url,Map<String,String> header,Map<String,String> params) {
        CloseableHttpResponse httpResponse = null;
        HttpResult httpResult = null;
        //连接池中获取httpclient对象
        CloseableHttpClient httpClient = defaultHttpClient();

        //Post请求对象
        HttpPost httpPost = new HttpPost(url);

        //连接默认配置
        defaultRequestConfig(httpPost);

        //封装请求头
        setHeader(header,httpPost);
        try {
            //封装请求参数
            setParams(params,httpPost);
            //获取请求结果
            httpResult = getHttpResult(httpClient,httpResponse,httpPost);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            if(httpResponse != null){
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(httpClient != null){
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return httpResult;
    }

//    private static void doPostJson(String url,Map<String,String> header,Map<String,String> params,PoolingHttpClientConnectionManager connectionManager){
//
//    }

    /**
     * 封装请求头
     * @param header
     * @param httpMethod
     */
    private static void setHeader(Map<String, String> header, HttpRequestBase httpMethod) {
        if(header != null){
            for(Map.Entry<String,String> entry : header.entrySet()){
                httpMethod.setHeader(entry.getKey(),entry.getValue());
            }
        }
    }

    /**
     * 封装请求参数
     * @param params
     * @param httpMethod
     * @throws UnsupportedEncodingException
     */
    private static void setParams(Map<String, String> params, HttpEntityEnclosingRequestBase httpMethod) throws UnsupportedEncodingException {
        if(params != null){
            List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
            for(Map.Entry<String,String> entry : params.entrySet()){
                nameValuePairList.add(new BasicNameValuePair(entry.getKey(),entry.getValue()));
            }
            httpMethod.setEntity(new UrlEncodedFormEntity(nameValuePairList,"UTF-8"));
        }
    }

    /**
     * 获取请求返回结果
     * @param httpClient
     * @param httpResponse
     * @param httpMethod
     * @return
     * @throws IOException
     */
    private static HttpResult getHttpResult(CloseableHttpClient httpClient, HttpResponse httpResponse, HttpRequestBase httpMethod) throws IOException {
        httpResponse = httpClient.execute(httpMethod);
        if(httpResponse != null && httpResponse.getStatusLine()!=null){
            String content = "";
            if(httpResponse.getEntity() != null){
                content = EntityUtils.toString(httpResponse.getEntity(),"UTF-8");
            }
            return new HttpResult(httpResponse.getStatusLine().getStatusCode(),content);
        }
        return new HttpResult(HttpStatus.SC_INTERNAL_SERVER_ERROR,"");
    }

    private static HttpRequestRetryHandler httpRequestRetryHandler(final int times){
        HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler() {
            @Override
            public boolean retryRequest(IOException e, int i, HttpContext httpContext) {
                //重试了i次，没有成功
                if(i >= times){
                    return false;
                }
                //服务器丢掉了连接,重试
                if(e instanceof NoHttpResponseException){
                    return false;
                }
                //SSL握手异常，不重试
                if(e instanceof SSLHandshakeException){
                    return false;
                }
                //超时
                if(e instanceof InterruptedIOException){
                    return false;
                }
                //目标服务器不可达
                if(e instanceof UnknownHostException){
                    return true;
                }
                //连接拒绝(??)
                if(e instanceof ConnectTimeoutException){
                    return false;
                }
                //SSL握手异常
                if(e instanceof SSLException){
                    return false;
                }
                HttpClientContext httpClientContext = HttpClientContext.adapt(httpContext);
                HttpRequest httpRequest = httpClientContext.getRequest();
                //请求是幂等的，再次尝试
                if(!(httpRequest instanceof HttpEntityEnclosingRequest)){
                    return true;
                }
                return false;
            }
        };
        return  httpRequestRetryHandler;
    }

    public static class IdleConnectionEvictor extends Thread{
        private HttpClientConnectionManager httpClientConnectionManager;
        private volatile boolean shutdown;

        public IdleConnectionEvictor(HttpClientConnectionManager connectionManager){
            this.httpClientConnectionManager =  connectionManager;
        }

        @Override
        public void run() {
            try {
                while(!shutdown){
                    synchronized (this){
                        wait(3000);
                        //关闭失效的连接
                        httpClientConnectionManager.closeExpiredConnections();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //是否被调用?????
        public void shutdown(){
            System.out.println("shutdown()被调用");
            shutdown = true;
            synchronized (this){
                notifyAll();
            }
        }
    }
}
