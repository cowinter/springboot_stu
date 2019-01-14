package com.zhishen.p_03.util.httpclient;

import ch.qos.logback.core.net.SyslogOutputStream;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.lang.reflect.Constructor;
import java.net.UnknownHostException;

public class SingleHttpConnManager {
    public static void main(String[] args){
        //连接池管理
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        //最大连接数
        connectionManager.setMaxTotal(200);
        //每个路由的最大连接数
        connectionManager.setDefaultMaxPerRoute(20);

        String url = "https://www.baidu.com/s?wd=mvnrepository";
        long start = System.currentTimeMillis();
        for(int i=0;i<3;i++){
            doGet(connectionManager,url); 
        }
        long end = System.currentTimeMillis();
        System.out.print("3次GET请求耗时=="+(end-start));
        //清理无效连接
        new IdleConnectionEvictor(connectionManager).start();
    }

    private static void doGet(PoolingHttpClientConnectionManager connectionManager, String url) {
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
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

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
        //是否被调用
        public void shutdown(){
            System.out.println("shutdown()被调用");
            shutdown = true;
            synchronized (this){
                notifyAll();
            }
        }
    }
}
