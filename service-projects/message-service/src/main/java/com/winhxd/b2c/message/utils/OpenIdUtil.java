package com.winhxd.b2c.message.utils;

import com.winhxd.b2c.common.domain.message.model.MiniOpenId;
import com.winhxd.b2c.common.util.JsonUtil;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
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
 * @className OpenIdUtil
 * @description
 */
@Component
public class OpenIdUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(OpenIdUtil.class);
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
     * 授权（必填）
     */
    @Value("${wechat.miniProgram.grantType}")
    private String grantType;
    /**
     * 根据code获取openid的url
     */
    @Value("${wechat.miniProgram.openidUrl}")
    private String openidUrl;

    @Autowired
    HttpClientUtil httpClientUtil;

    public MiniOpenId oauth2GetOpenid(String code){
        MiniOpenId miniOpenId = new MiniOpenId();
        //请求参数
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("appid", appid));
        params.add(new BasicNameValuePair("secret", secret));
        params.add(new BasicNameValuePair("js_code", code));
        params.add(new BasicNameValuePair("grant_type", grantType));
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
                miniOpenId.setOpenId(openid);
                miniOpenId.setSessionKey(sessionKey);
                miniOpenId.setUnionId(unionId);
                LOGGER.info("OpenIdUtil ->oauth2GetOpenid,小程序根据code获取openid，openid为={}",openid);
            }else{
                LOGGER.error("OpenIdUtil ->oauth2GetOpenid,小程序根据code获取openid出错，需查询错误码，错误码为={}",errcode);
                return null;
            }
        } catch (IOException e) {
            LOGGER.error("OpenIdUtil ->oauth2GetOpenid,小程序根据code获取openid出错，异常信息为={}",e);
        } catch (URISyntaxException e) {
            LOGGER.error("OpenIdUtil ->oauth2GetOpenid,小程序根据code获取openid出错，异常信息为={}",e);
        }
        return miniOpenId;
    }

}
