package com.winhxd.b2c.message.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.winhxd.b2c.common.domain.store.vo.QRCodeInfoVO;
import com.winhxd.b2c.common.domain.store.vo.StoreUserInfoVO;
import com.winhxd.b2c.common.domain.system.login.condition.StoreUserInfoCondition;
import com.winhxd.b2c.common.feign.store.StoreServiceClient;
import com.winhxd.b2c.common.util.JsonUtil;
import com.winhxd.b2c.message.service.WechatShareService;
import com.winhxd.b2c.message.utils.HttpClientUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.*;

/**
 * @author chengyy
 * @Description: 微信分享业务实现类
 * @date 2018/8/4 13:34
 */
@Service
public class WechatShareServiceImpl implements WechatShareService {

    private ObjectMapper objectMapper = new ObjectMapper();

    private Logger logger = LoggerFactory.getLogger(WechatShareServiceImpl.class);

    @Autowired
    private HttpClientUtil httpClientUtil;

    /**
     * 获取token的url地址
     */
    @Value("${wechat.share.tokenUrl}")
    private String tokenUrl;

    /**
     * token认证类型
     */
    @Value("${wechat.share.grantType}")
    private String grantType;

    /**
     * 第三方用户唯一凭证
     */
    @Value("${wechat.share.appid}")
    private String appid;

    /**
     * 第三方用户唯一凭证密码
     */
    @Value("${wechat.share.secret}")
    private String secret;

    /**
     * 二维码生成的url地址
     */
    @Value("${wechat.share.codeUrl}")
    private String codeUrl;

    /**
     * 扫描二维码跳转默认页面
     */
    @Value("${wechat.share.pageUrl}")
    private String pageUrl;

    /**
     * 小程序二维码宽度
     */
    @Value("${wechat.share.width}")
    private int width;

    /**
     * 自动配置线条颜色
     */
    @Value("${wechat.share.autoColor}")
    private boolean autoColor;

    /**
     * 是否需要透明底色
     */
    @Value("${wechat.share.isHyaline}")
    private boolean isHyaline;

    @Autowired
    private StoreServiceClient storeServiceClient;


    private String baseHost = "http://upload.winhxd.com:8100/crm/uploadResource2CDNAction.do?method=addRecordNewJson";

    /**
     * @param storeUserId 门店id
     * @return 二进制数据
     * @author chengyy
     * @date 2018/8/4 14:33
     * @Description 生成小程序二维码并返回二进制数据
     */
    @Override
    public QRCodeInfoVO generateQRCodePic(Long storeUserId) {
        //先从数据库中查询当前的门店有没有小程序码信息
        StoreUserInfoVO storeUserInfoVO = storeServiceClient.findStoreUserInfo(storeUserId).getData();
        if (storeUserInfoVO == null) {
            return null;
        }
        if (!StringUtils.isEmpty(storeUserInfoVO.getMiniProgramCodeUrl())) {
            QRCodeInfoVO codeInfoVO = new QRCodeInfoVO();
            codeInfoVO.setMiniProgramCodeUrl(storeUserInfoVO.getMiniProgramCodeUrl());
            codeInfoVO.setStoreName(storeUserInfoVO.getStoreName());
            return codeInfoVO;
        }
        //获取token
        String token = getToken();
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        //请求小程序码图片数据
        String url = codeUrl + "?access_token=" + token;
        CloseableHttpClient client = httpClientUtil.getHttpClient();
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Content-Type", "application/json");
        Map params = new HashMap<>();
        params.put("scene", "storeUserId=" + storeUserId);
        params.put("path", pageUrl);
        params.put("width", width);
        params.put("auto_color", false);
        Map<String, Object> line_color = new HashMap<>();
        line_color.put("r", 0);
        line_color.put("g", 0);
        line_color.put("b", 0);
        params.put("line_color", line_color);
        CloseableHttpResponse response = null;
        try {
            httpPost.setEntity(new StringEntity(objectMapper.writeValueAsString(params), "UTF-8"));
            response = client.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                //将返回的图片数据上传保存到CDN
                String fileName = UUID.randomUUID().toString().replace("-", "")+".png";
                //把获取到的二进制图片直接作为请求数据实体进行提交
                HttpPost uploadPost = new HttpPost(baseHost);
                MultipartEntityBuilder builder = MultipartEntityBuilder.create();
                // 文件流
                builder.addBinaryBody("file", response.getEntity().getContent(), ContentType.MULTIPART_FORM_DATA, fileName);
                // 类似浏览器表单提交，对应input的name和value
                builder.addTextBody("filename", fileName);
                HttpEntity entity = builder.build();
                uploadPost.setEntity(entity);
                // 执行提交
                response = client.execute(uploadPost);
                if (response.getStatusLine().getStatusCode() == 200) {
                    // 将响应内容转换为字符串
                    String result = EntityUtils.toString(response.getEntity(), Charset.forName("UTF-8"));
                    if (result != null) {
                        Map<String, Object> json = JsonUtil.parseJSONObject(result);
                        String path = (String) json.get("path");
                        if (!StringUtils.isEmpty(path)) {
                            StoreUserInfoCondition condition = new StoreUserInfoCondition();
                            condition.setId(storeUserInfoVO.getId());
                            condition.setMiniProgramCodeUrl(path);
                            storeServiceClient.saveStoreCodeUrl(condition);
                            QRCodeInfoVO codeInfoVO = new QRCodeInfoVO();
                            codeInfoVO.setMiniProgramCodeUrl(path);
                            codeInfoVO.setStoreName(storeUserInfoVO.getStoreName());
                            return codeInfoVO;
                        }
                    }
                }


            }
        } catch (IOException e) {
            logger.error("WechatShareServiceImpl ->fetchQRCodePic生成二维码时网络错误,错误信息为={}", e);
            e.printStackTrace();


        } finally {
            try {
                response.close();
            } catch (IOException e) {
                logger.error("WechatShareServiceImpl ->fetchQRCodePic关闭流报错,错误信息为={}", e);
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     * @return token数据
     * @author chengyy
     * @date 2018/8/4 13:52
     * @Description 微信小程序认证获取token
     */
    public String getToken() {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("grant_type", grantType));
        params.add(new BasicNameValuePair("appid", appid));
        params.add(new BasicNameValuePair("secret", secret));
        String token = null;
        try {
            String content = httpClientUtil.doGet(tokenUrl, params);
            if (StringUtils.isEmpty(content)) {
                return null;
            }
            JsonNode node = objectMapper.readTree(content);
            token = node.get("access_token").asText();
            return token;
        } catch (URISyntaxException e) {
            logger.error("WechatShareServiceImpl ->getToken获取token是请求的url地址错误tokenUrl={}", tokenUrl);
            e.printStackTrace();
        } catch (IOException e) {
            logger.error("WechatShareServiceImpl ->getToken获取token是请求网络错误msg={}", e);
            e.printStackTrace();
        } finally {
            return token;
        }
    }
}
