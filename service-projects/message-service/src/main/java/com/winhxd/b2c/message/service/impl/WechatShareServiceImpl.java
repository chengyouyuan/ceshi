package com.winhxd.b2c.message.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.winhxd.b2c.message.service.WechatShareService;
import com.winhxd.b2c.message.utils.HttpClientUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
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
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 微信分享业务实现类
 * @author chengyy
 * @date 2018/8/4 13:34
 */
@Service
public class WechatShareServiceImpl implements WechatShareService {

    private ObjectMapper objectMapper = new ObjectMapper();

    private Logger logger = LoggerFactory.getLogger(WechatShareServiceImpl.class);

    @Autowired
    private HttpClientUtil httpClientUtil;

    /**获取token的url地址*/
    @Value("${wechat.share.tokenUrl}")
    private String tokenUrl;

    /**token认证类型*/
    @Value("${wechat.share.grantType}")
    private String grantType;

    /**第三方用户唯一凭证*/
    @Value("${wechat.share.appid}")
    private String appid;

    /**第三方用户唯一凭证密码*/
    @Value("${wechat.share.secret}")
    private String secret;

    /**二维码生成的url地址*/
    @Value("${wechat.share.codeUrl}")
    private String codeUrl;

    /**扫描二维码跳转默认页面*/
    @Value("${wechat.share.pageUrl}")
    private String pageUrl;

    /**小程序二维码宽度*/
    @Value("${wechat.share.width}")
    private int width;

    /**自动配置线条颜色*/
    @Value("${wechat.share.autoColor}")
    private boolean autoColor;

    /**是否需要透明底色*/
    @Value("${wechat.share.isHyaline}")
    private boolean isHyaline;
    /**
     * @author chengyy
     * @date 2018/8/4 14:33
     * @Description 生成小程序二维码并返回二进制数据
     * @param storeUserId 门店id
     * @return 二进制数据
     */
    @Override
    public byte[] generateQRCodePic(Long storeUserId){
        //获取token
        String token = getToken();
        if(StringUtils.isEmpty(token)){
            return null;
        }
        return fetchQRCodePic(token,storeUserId);
    }
    /**
     * @author chengyy
     * @date 2018/8/4 13:58
     * @Description 通过token从微信接口获取生成小程序二维码
     * @param token token令牌
     * @param storeUserId 门店id
     * @return  二进制数据
     */
    private byte[] fetchQRCodePic(String token,Long storeUserId){
        String url = codeUrl+"?access_token="+token;
        CloseableHttpClient client = httpClientUtil.getHttpClient();
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Content-Type", "application/json");
        Map params = new HashMap<>();
        params.put("scene","storeUserId="+storeUserId);
        params.put("path",pageUrl);
        params.put("width",width);
        params.put("auto_color",false);
        Map<String,Object> line_color = new HashMap<>();
        line_color.put("r", 0);
        line_color.put("g", 0);
        line_color.put("b", 0);
        params.put("line_color", line_color);
        CloseableHttpResponse response = null;
        try {
            StringEntity entity = new StringEntity(objectMapper.writeValueAsString(params),"UTF-8");
            httpPost.setEntity(entity);
            response = client.execute(httpPost);
            if(response.getStatusLine().getStatusCode() == 200){
                return  EntityUtils.toByteArray(response.getEntity());
            }
        } catch (IOException e) {
            logger.error("WechatShareServiceImpl ->fetchQRCodePic生成二维码时网络错误,错误信息为={}",e);
            e.printStackTrace();

        } finally {
            try {
                response.close();
            } catch (IOException e) {
                logger.error("WechatShareServiceImpl ->fetchQRCodePic关闭流报错,错误信息为={}",e);
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * @author chengyy
     * @date 2018/8/4 13:52
     * @Description 微信小程序认证获取token
     * @return token数据
     */
    public String getToken(){
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("grant_type",grantType));
        params.add(new BasicNameValuePair("appid",appid));
        params.add(new BasicNameValuePair("secret",secret));
        String token = null;
        try {
            String content =  httpClientUtil.doGet(tokenUrl,params);
            if(StringUtils.isEmpty(content)){
                return null;
            }
            JsonNode node = objectMapper.readTree(content);
            token = node.get("access_token").asText();
            return token;
        } catch (URISyntaxException e) {
            logger.error("WechatShareServiceImpl ->getToken获取token是请求的url地址错误tokenUrl={}",tokenUrl);
            e.printStackTrace();
        } catch (IOException e) {
            logger.error("WechatShareServiceImpl ->getToken获取token是请求网络错误msg={}",e);
            e.printStackTrace();
        } finally {
            return token;
        }
    }
}
