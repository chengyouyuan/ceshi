package com.winhxd.b2c.message.utils;

import com.winhxd.b2c.common.cache.Cache;
import com.winhxd.b2c.common.constant.CacheName;
import com.winhxd.b2c.common.domain.message.model.MiniOpenId;
import com.winhxd.b2c.common.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author jujinbiao
 * @className MiniProgramUtils
 * @description
 */
@Component
public class MiniProgramUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(MiniProgramUtils.class);
    private static final String RETURN_NULL = "null";
    /**
     * 小程序APPID
     */
    @Value("${wechat.miniProgram.appid}")
    private String appid;
    /**
     * 小程序SECRET
     */
    @Value("${wechat.miniProgram.secret}")
    private String secret;
    /**
     * 授权（必填）获取openid
     */
    @Value("${wechat.miniProgram.grantTypeAuth}")
    private String grantTypeAuth;
    /**
     * 授权（必填）获取accessToken
     */
    @Value("${wechat.miniProgram.grantTypeCre}")
    private String grantTypeCre;
    /**
     * 根据code获取openid的url
     */
    @Value("${wechat.miniProgram.openidUrl}")
    private String openidUrl;
    /**
     * 获取accessTokenUrl
     */
    @Value("${wechat.miniProgram.accessTokenUrl}")
    private String accessTokenUrl;
    /**
     * 发送小程序模板消息sendMsgUrl
     */
    @Value("${wechat.miniProgram.sendMsgUrl}")
    private String sendMsgUrl;

    @Autowired
    HttpClientUtil httpClientUtil;
    @Autowired
    Cache cache;

    public MiniOpenId oauth2GetOpenid(String code){
        MiniOpenId miniOpenId = new MiniOpenId();
        //请求参数
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("appid", appid));
        params.add(new BasicNameValuePair("secret", secret));
        params.add(new BasicNameValuePair("js_code", code));
        params.add(new BasicNameValuePair("grant_type", grantTypeAuth));
        try {
            LOGGER.info("OpenIdUtil ->oauth2GetOpenid,小程序根据code获取openid，code为={}",code);
            //发送请求
            String data = httpClientUtil.doGet(openidUrl,params);
            //解析相应内容（转换成json对象）
            Map map = JsonUtil.parseJSONObject(data);
            //错误码，成功的时候不会返回,获取到的是"null"(字符串null)，错误的时候返回错误码
            String errcode = String.valueOf(map.get("errcode"));
            if (RETURN_NULL.equals(errcode)){
                //用户的唯一标识（openid）,获取不到的时候返回"null"(字符串null)
                String openid =String.valueOf(map.get("openid"));
                String sessionKey =String.valueOf(map.get("session_key"));
                String unionId =String.valueOf(map.get("unionid"));
                miniOpenId.setOpenid(openid);
                miniOpenId.setSessionKey(sessionKey);
                miniOpenId.setUnionid(unionId);
                LOGGER.info("MiniProgramUtils ->oauth2GetOpenid,小程序根据code获取openid，openid为={}",openid);
            }else{
                LOGGER.error("MiniProgramUtils ->oauth2GetOpenid,小程序根据code获取openid出错，需查询错误码，错误码为={}",errcode);
                return null;
            }
        } catch (IOException e) {
            LOGGER.error("MiniProgramUtils ->oauth2GetOpenid,小程序根据code获取openid出错，异常信息为={}",e);
        } catch (URISyntaxException e) {
            LOGGER.error("MiniProgramUtils ->oauth2GetOpenid,小程序根据code获取openid出错，异常信息为={}",e);
        }
        return miniOpenId;
    }

    /**
     * 小程序获取AccessToken
     * @return
     */
    public String getAccessToken(){
        String token = null;
        //先看缓存中有没有，如果没有，重新获取，再存到缓存中。
        if(StringUtils.isNotEmpty(cache.get(CacheName.MESSAGE_MINI_ACCESS_TOKEN))){
            token = cache.get(CacheName.MESSAGE_MINI_ACCESS_TOKEN);
            LOGGER.info("MiniProgramUtils ->getAccessToken,小程序获取AccessToken，cache.AccessToken={}",token);
            return token;
        }
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("grant_type",grantTypeCre));
        params.add(new BasicNameValuePair("appid",appid));
        params.add(new BasicNameValuePair("secret",secret));

        try {
            String content =  httpClientUtil.doGet(accessTokenUrl,params);
            if(StringUtils.isEmpty(content)){
                return null;
            }
            Map<String, Object> accessMap = JsonUtil.parseJSONObject(content);
            if (accessMap != null && accessMap.get("access_token") != null){
                token = String.valueOf(accessMap.get("access_token"));
                cache.set(CacheName.MESSAGE_MINI_ACCESS_TOKEN,token);
                cache.expire(CacheName.MESSAGE_MINI_ACCESS_TOKEN,90 * 60);
            }
            LOGGER.info("MiniProgramUtils ->getAccessToken,小程序获取AccessToken，小程序获取AccessToken={}",token);
            return token;
        } catch (URISyntaxException e) {
            LOGGER.error("MiniProgramUtils ->getAccessToken,小程序获取AccessToken出错，异常信息为={}",e);
        } catch (IOException e) {
            LOGGER.error("MiniProgramUtils ->getAccessToken,小程序获取AccessToken出错，异常信息为={}",e);
        } finally {
        }
        return token;
    }

    public String sendMiniMsg(String msgJson) throws IOException{
        String accessToken = getAccessToken();
        if (StringUtils.isEmpty(accessToken)){
            LOGGER.error("MiniProgramUtils ->sendMiniMsg,小程序获取AccessToken为空");
            return null;
        }
        String url = sendMsgUrl+"?access_token="+accessToken;
        // 创建http POST请求
        CloseableHttpClient client = httpClientUtil.getHttpClient();
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Content-Type", "application/json");
        CloseableHttpResponse response = null;
        String content = null;
        try {
            // 构造一个form表单式的实体
            StringEntity entity = new StringEntity(msgJson,"UTF-8");
            httpPost.setEntity(entity);
            // 执行请求
            response = client.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                // 判断返回状态是否为200
                content = EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return content;
    }

}
