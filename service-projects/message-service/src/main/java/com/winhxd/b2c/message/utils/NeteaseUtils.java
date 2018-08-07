package com.winhxd.b2c.message.utils;

import com.winhxd.b2c.common.util.JsonUtil;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

/**
 * @author jujinbiao
 * @className NeteaseUtil 云信工具类
 * @description
 */
@Component
public class NeteaseUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(NeteaseUtils.class);
    /**
     * 请求超时
     */
    private static final int TIME_OUT = 3000;
    /**
     * 创建网易云通信ID
     */
    private static final String CREATE_ACCOUNT_URL = "https://api.netease.im/nimserver/user/create.action";
    /**
     * 获取用户名片
     */
    private static final String USER_INFO_URL = "https://api.netease.im/nimserver/user/getUinfos.action";
    /**
     * 更新用户名片
     */
    private static final String UPDATE_ACCOUNT_URL = "https://api.netease.im/nimserver/user/updateUinfo.action";

    @Value("${netease.appKey}")
    private String appKey;
    @Value("${netease.appSecret}")
    private String appSecret;

    @Autowired
    HttpClientUtil httpClientUtil;

    /**
     * 调用云信接口，发送请求
     * @param url 请求url
     * @param nvps 参数
     * @return
     */
    public Map<String,Object> sendHttpClientPost(String url, List<NameValuePair> nvps) {
        Map<String,Object> result = new HashMap<>();
        try {
            CloseableHttpClient httpClient = httpClientUtil.getHttpClient();
            HttpPost httpPost = new HttpPost(url);
            String curTime = String.valueOf(((new Date()).getTime()-5000) / 1000L);
            String nonce = "hdaskdjkasdkasjdka";
            String checkSum = CheckSumBuilder.getCheckSum(appSecret, nonce ,curTime);
            // 设置请求的header
            httpPost.addHeader("AppKey", appKey);
            httpPost.addHeader("Nonce", nonce);
            httpPost.addHeader("CurTime", curTime);
            httpPost.addHeader("CheckSum", checkSum);
            httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            //设置云信链接超时时间
            httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, TIME_OUT);
            httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, TIME_OUT);
            // 设置请求的参数
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));

            // 执行请求
            HttpResponse response = httpClient.execute(httpPost);
            result = JsonUtil.parseJSONObject(EntityUtils.toString(response.getEntity(), "utf-8"));
            httpClient.close();
        }  catch (IOException e) {
            LOGGER.error("NeteaseUtils ->sendHttpClientPost,发送云信http请求出错，异常信息为={}", e);
        }   catch (Exception e){
            LOGGER.error("NeteaseUtils ->sendHttpClientPost,发送云信http请求出错，异常信息为={}",e);
        }
        return result;
    }

    /**
     * 获取云信用户信息
     * @param accids
     * @return
     */
    public Map<String,Object> getUserInfo(String accids){
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("accids", accids));
        return sendHttpClientPost(USER_INFO_URL, nvps);
    }

    /**
     * 修改云信账户密码
     * @param accid
     * @param token
     * @return
     */
    public Map<String,Object> updateUserInfo(String accid,String token){
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("accid", accid));
        nvps.add(new BasicNameValuePair("token", token));
        return sendHttpClientPost(UPDATE_ACCOUNT_URL, nvps);
    }

    /**
     * 创建云信账号
     * @param accid
     * @param token
     * @param name
     * @param icon
     * @return
     */
    public Map<String,Object> createAccount(String accid,String token,String name,String icon){
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("accid", accid));
        nvps.add(new BasicNameValuePair("token", token));
        nvps.add(new BasicNameValuePair("name", name));
        nvps.add(new BasicNameValuePair("icon", icon));
        return sendHttpClientPost(CREATE_ACCOUNT_URL, nvps);
    }

}
