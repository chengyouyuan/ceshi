package com.winhxd.b2c.message.utils;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.winhxd.b2c.common.context.support.ContextHelper;
import com.winhxd.b2c.common.domain.message.condition.NeteaseMsg;
import com.winhxd.b2c.common.domain.message.condition.NeteaseMsgCondition;
import com.winhxd.b2c.common.util.JsonUtil;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
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
    private static final String NETEASE_PLATFORM_ADMIN = "admin";
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
    /**
     * 发送普通消息
     */
    private static final String SEND_MSG_URL = "https://api.netease.im/nimserver/msg/sendMsg.action";
    /**
     * 批量发送点对点普通消息
     */
    private static final String SEND_BATCH_MSG_URL = "https://api.netease.im/nimserver/msg/sendBatchMsg.action";

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
            // 设置请求的参数
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, ContextHelper.UTF_8));

            // 执行请求
            HttpResponse response = httpClient.execute(httpPost);
            result = JsonUtil.parseJSONObject(EntityUtils.toString(response.getEntity(), ContextHelper.UTF_8));
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
    public Map<String,Object> updateUserToken(String accid,String token){
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("accid", accid));
        nvps.add(new BasicNameValuePair("token", token));
        return sendHttpClientPost(UPDATE_ACCOUNT_URL, nvps);
    }

    /**
     * 修改云信账户信息
     * @param accid
     * @param name
     * @param icon
     * @return
     */
    public Map<String,Object> updateUserInfo(String accid,String name,String icon){
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("accid", accid));
        nvps.add(new BasicNameValuePair("name", name));
        nvps.add(new BasicNameValuePair("icon", icon));
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

    /**
     * 发送普通云信消息
     * @param accid
     * @param neteaseMsgCondition
     * @return
     */
    public Map<String,Object> sendTxtMessage2Person(String accid,NeteaseMsgCondition neteaseMsgCondition){
        String bodyMsg = buildBodyJsonMsg(neteaseMsgCondition.getNeteaseMsg().getMsgContent());
        //扩展参数
        String extMsg = buildExtJsonMsg(neteaseMsgCondition.getNeteaseMsg());
        //组织参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("from", NETEASE_PLATFORM_ADMIN));
        //ope：0表示点对点个人消息
        nvps.add(new BasicNameValuePair("ope", "0"));
        nvps.add(new BasicNameValuePair("to", accid));
        //type:0表示文本消息
        nvps.add(new BasicNameValuePair("type", "0"));
        nvps.add(new BasicNameValuePair("body", bodyMsg));
        nvps.add(new BasicNameValuePair("ext", extMsg));
        return  sendHttpClientPost(SEND_MSG_URL,nvps);
    }

    private String buildBodyJsonMsg(String msgContent) {
        ObjectNode bodyJson = JsonUtil.createObjectNode();
        bodyJson.put("type","txt");
        bodyJson.put("msg",msgContent);
        return bodyJson.toString();
    }

    public static String buildExtJsonMsg(NeteaseMsg neteaseMsg){
        ObjectNode extJson = JsonUtil.createObjectNode();
        extJson.put("title",neteaseMsg.getMsgContent());
        extJson.put("pagetype",neteaseMsg.getPageType());
        extJson.put("audiotype", neteaseMsg.getAudioType());
        extJson.put("page", neteaseMsg.getTreeCode());
        return extJson.toString();
    }

    public Map<String,Object> sendTxtMessage2Batch(String[] accids,String content){
        String bodyMsg = buildBodyJsonMsg(content);
        //组织参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("fromAccid", NETEASE_PLATFORM_ADMIN));
        nvps.add(new BasicNameValuePair("toAccids", JsonUtil.toJSONString(accids)));
        //type:0表示文本消息
        nvps.add(new BasicNameValuePair("type", "0"));
        nvps.add(new BasicNameValuePair("body", bodyMsg));
        return  sendHttpClientPost(SEND_BATCH_MSG_URL,nvps);
    }

}
