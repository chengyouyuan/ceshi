package com.winhxd.b2c.message.utils;

import com.winhxd.b2c.common.context.support.ContextHelper;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
/**
 * @Description: httpClient帮助类，可以用来进行http的get和post请求
 * @author chengyy
 * @date 2018/8/4 11:27
 */
@Component
public class HttpClientUtil implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    public CloseableHttpClient getHttpClient(){
       return applicationContext.getBean(CloseableHttpClient.class);
    }


   /**
    * @author chengyy
    * @date 2018/8/4 11:13
    * @Description httpclient进行get带参请求
    * @param  params 请求参数
    * @return  请求的结果
    * @exception
    */
    public String doGet(String url, List<NameValuePair> params) throws URISyntaxException, IOException {
        URIBuilder uriBuilder = new URIBuilder(url);
        if(params != null && params.size() > 0){
            uriBuilder.setParameters(params);
        }
        URI  uri = uriBuilder.build();
        // 创建http GET请求
        HttpGet httpGet = new HttpGet(uri);
        CloseableHttpResponse response = null;
        String content = null;
        try {
            // 执行请求
            response = getHttpClient().execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                content = EntityUtils.toString(response.getEntity(), ContextHelper.UTF_8);
                return content;
            }
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return content;
    }

    public String doGet(String url) throws IOException, URISyntaxException {
       return doGet(url,null);
    }

    /**
     * @author chengyy
     * @date 2018/8/4 11:23
     * @Description post无参请求
     * @param
     * @return
     * @exception
     */
    public String doPost(String url) throws IOException {
        return doPost(url,null);
    }
    /**
     * @author chengyy
     * @date 2018/8/4 11:18
     * @Description httpclient 的post带参请求
     * @param url 地址
     * @param params 请求的参数
     * @return   返回结果字符串
     */
    public String doPost(String url, List<NameValuePair> params) throws IOException {
        // 创建http POST请求
        HttpPost httpPost = new HttpPost(url);
        // 构造一个form表单式的实体
        if (params != null && params.size() > 0){
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(params);
            // 将请求实体设置到httpPost对象中
            httpPost.setEntity(formEntity);
        }
        CloseableHttpResponse response = null;
        String content = null;
        try {
            // 执行请求
            response = getHttpClient().execute(httpPost);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                content = EntityUtils.toString(response.getEntity(), ContextHelper.UTF_8);
            }
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return content;
    }
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext =  applicationContext;
    }
}
