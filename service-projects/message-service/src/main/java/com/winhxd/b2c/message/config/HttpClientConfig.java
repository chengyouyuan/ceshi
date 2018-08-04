package com.winhxd.b2c.message.config;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * @Description: httpclient配置类
 * @author chengyy
 * @date 2018/8/4 10:07
 */
@Configuration
public class HttpClientConfig {
    @Value("${httpClient.maxTotal}")
    private int maxTotal;

    @Value("${httpClient.maxPerRoute}")
    private int maxPerRoute;

    @Value("${httpClient.requestTimeout}")
    private int requestTimeout;

    @Value("${httpClient.socketTimeout}")
    private int socketTimeout;

    @Bean(destroyMethod = "close")
    public PoolingHttpClientConnectionManager poolingHttpClientConnectionManager(){
        PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager();
        //连接池最大并发连接数
        manager.setMaxTotal(maxTotal);
        //将每个路由基础的连接增加
        manager.setDefaultMaxPerRoute(maxPerRoute);
        return manager;
    }

    @Bean
    @Scope("prototype")
    public CloseableHttpClient httpClient(){
        return HttpClientBuilder.create().setConnectionManager(poolingHttpClientConnectionManager()).
                setDefaultRequestConfig(requestConfig()).build();
    }

    @Bean
    public RequestConfig requestConfig(){
        //创建连接的最长时间
        RequestConfig config = RequestConfig.custom().setConnectTimeout(1000)
                //从连接池中获取到连接的最长时间
                .setConnectionRequestTimeout(requestTimeout)
                //数据传输的最长时间
                .setSocketTimeout(socketTimeout)
                //提交请求前测试连接是否可用
                .setStaleConnectionCheckEnabled(true)
                .build();
        return config;
    }



}
