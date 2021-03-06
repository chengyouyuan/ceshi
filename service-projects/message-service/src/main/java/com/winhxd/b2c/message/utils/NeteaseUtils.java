package com.winhxd.b2c.message.utils;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.winhxd.b2c.common.context.support.ContextHelper;
import com.winhxd.b2c.common.domain.message.condition.NeteaseMsg;
import com.winhxd.b2c.common.domain.message.condition.NeteaseMsgCondition;
import com.winhxd.b2c.common.domain.message.enums.MsgPageTypeEnum;
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
    public Map<String,Object> sendTxtMessage2Person(String accid,NeteaseMsgCondition neteaseMsgCondition,String msgId){
        String bodyMsg = buildBodyJsonMsg(neteaseMsgCondition.getNeteaseMsg().getMsgContent());
        //扩展参数
        ObjectNode extMsg = buildExtJsonMsg(neteaseMsgCondition.getNeteaseMsg(),msgId);
        //组织参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("from", NETEASE_PLATFORM_ADMIN));
        //ope：0表示点对点个人消息
        nvps.add(new BasicNameValuePair("ope", "0"));
        nvps.add(new BasicNameValuePair("to", accid));
        //type:0表示文本消息
        nvps.add(new BasicNameValuePair("type", "0"));
        nvps.add(new BasicNameValuePair("body", bodyMsg));
        nvps.add(new BasicNameValuePair("ext", extMsg.toString()));
        Map<String, Object> extJsonMsg = JsonUtil.parseJSONObject(String.valueOf(extMsg.get("extJsonMsg")));
        extJsonMsg.put("sessionId",NETEASE_PLATFORM_ADMIN);
        nvps.add(new BasicNameValuePair("payload", JsonUtil.toJSONString(extJsonMsg)));
        nvps.add(new BasicNameValuePair("option", buildOptionJson()));
        LOGGER.info("NeteaseUtils sendTxtMessage2Person   ^^^^^^^^^   发送云信消息 请求参数"+nvps.toString());
        return sendHttpClientPost(SEND_MSG_URL,nvps);
    }

    private String buildOptionJson(){
        ObjectNode optionJson = JsonUtil.createObjectNode();
        optionJson.put("needPushNick","false");
        return optionJson.toString();
    }

    private String buildBodyJsonMsg(String msgContent) {
        ObjectNode bodyJson = JsonUtil.createObjectNode();
        bodyJson.put("type","txt");
        bodyJson.put("msg",msgContent == null ? "" : msgContent);
        return bodyJson.toString();
    }

    /**
     * 发单个消息用，消息弹窗，需要msgId来处理已读未读
     * @param neteaseMsg
     * @param msgId
     * @return
     */
    public static ObjectNode buildExtJsonMsg(NeteaseMsg neteaseMsg,String msgId){
        ObjectNode extJsonMsg =  JsonUtil.createObjectNode();
        ObjectNode extJson = JsonUtil.createObjectNode();
        extJson.put("title",neteaseMsg.getMsgContent() == null ? "" : neteaseMsg.getMsgContent());
        extJson.put("pagetype",String.valueOf(neteaseMsg.getPageType()));
        extJson.put("audiotype", String.valueOf(neteaseMsg.getAudioType()));
        extJson.put("page", neteaseMsg.getTreeCode() == null ? "" : neteaseMsg.getTreeCode());
        extJson.put("msgId",msgId == null ? "" : msgId);
        if(neteaseMsg.getAudioType() == 1){
            //文字转语音
            extJson.put("transferaudio","1");
        }else{
            //不转语音
            extJson.put("transferaudio","0");
        }
        extJsonMsg.put("extJsonMsg",extJson);
        return extJsonMsg;
    }

    /**
     * 批量推送消息用
     * @param content
     * @return
     */
    public static ObjectNode buildExtJsonMsg4Batch(String content){
        ObjectNode extJsonMsg =  JsonUtil.createObjectNode();
        ObjectNode extJson = JsonUtil.createObjectNode();
        extJson.put("title",content);
        extJson.put("pagetype",String.valueOf(MsgPageTypeEnum.NOTICE.getPageType()));
        extJson.put("audiotype", String.valueOf("0"));
        extJson.put("page", "");
        extJson.put("transferaudio","0");
        extJsonMsg.put("extJsonMsg",extJson);
        return extJsonMsg;
    }

    /**
     * 保存用，组织参数
     * @param neteaseMsg
     * @return
     */
    public static String buildExtJsonMsg4Save(NeteaseMsg neteaseMsg){
        ObjectNode extJsonMsg =  JsonUtil.createObjectNode();
        ObjectNode extJson = JsonUtil.createObjectNode();
        extJson.put("title",neteaseMsg.getMsgContent() == null ? "" : neteaseMsg.getMsgContent());
        extJson.put("pagetype",String.valueOf(neteaseMsg.getPageType()));
        extJson.put("audiotype", String.valueOf(neteaseMsg.getAudioType()));
        extJson.put("page", neteaseMsg.getTreeCode() == null ? "" : neteaseMsg.getTreeCode());
        if(neteaseMsg.getAudioType() == 1){
            //文字转语音
            extJson.put("transferaudio","1");
        }else{
            //不转语音
            extJson.put("transferaudio","0");
        }
        extJsonMsg.put("extJsonMsg",extJson);
        return extJsonMsg.toString();
    }

    public Map<String,Object> sendTxtMessage2Batch(String[] accids,String content){
        String bodyMsg = buildBodyJsonMsg(content);
        ObjectNode extMsg = buildExtJsonMsg4Batch(content);
        //组织参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("fromAccid", NETEASE_PLATFORM_ADMIN));
        nvps.add(new BasicNameValuePair("toAccids", JsonUtil.toJSONString(accids)));
        //type:0表示文本消息
        nvps.add(new BasicNameValuePair("type", "0"));
        nvps.add(new BasicNameValuePair("body", bodyMsg));
        nvps.add(new BasicNameValuePair("ext", extMsg.toString()));
        Map<String, Object> extJsonMsg = JsonUtil.parseJSONObject(String.valueOf(extMsg.get("extJsonMsg")));
        extJsonMsg.put("sessionId",NETEASE_PLATFORM_ADMIN);
        nvps.add(new BasicNameValuePair("payload", JsonUtil.toJSONString(extJsonMsg)));
        nvps.add(new BasicNameValuePair("option", buildOptionJson()));
        LOGGER.info("NeteaseUtils sendTxtMessage2Batch   ^^^^^^^^^   发送云信消息 请求参数"+nvps.toString());
        return  sendHttpClientPost(SEND_BATCH_MSG_URL,nvps);
    }

}
