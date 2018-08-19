package com.winhxd.b2c.message.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.common.model.BaseImageFile;
import com.winhxd.b2c.common.domain.message.vo.MiniProgramConfigVO;
import com.winhxd.b2c.common.domain.store.vo.ProductImageVO;
import com.winhxd.b2c.common.domain.store.vo.QRCodeInfoVO;
import com.winhxd.b2c.common.domain.store.vo.StoreUserInfoVO;
import com.winhxd.b2c.common.domain.system.login.condition.StoreUserInfoCondition;
import com.winhxd.b2c.common.feign.store.StoreServiceClient;
import com.winhxd.b2c.common.util.ImageUploadUtil;
import com.winhxd.b2c.message.service.WechatShareService;
import com.winhxd.b2c.message.utils.HttpClientUtil;
import com.winhxd.b2c.message.utils.MiniProgramUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
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

    @Autowired
    private ImageUploadUtil imageUploadUtil;

    @Autowired
    private MiniProgramUtils miniProgramUtils;

    @Value("${wechat.config.path}")
    private String path;

    @Value("${wechat.config.title}")
    private String title;

    @Value("${wechat.config.userName}")
    private String userName;

    @Value("${wechat.config.description}")
    private String description;

    @Value("${wechat.config.transaction}")
    private String transaction;

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
        String token = miniProgramUtils.getAccessToken();
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
        params.put("auto_color", autoColor);
        params.put("is_hyaline",isHyaline);
        CloseableHttpResponse response = null;
        try {
            httpPost.setEntity(new StringEntity(objectMapper.writeValueAsString(params), "UTF-8"));
            response = client.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                //将返回的图片数据上传保存到CDN
                String fileName = UUID.randomUUID().toString().replace("-", "") + ".png";
                //上传图片
                BaseImageFile baseImageFile = imageUploadUtil.uploadImage(fileName, response.getEntity().getContent(), null);
                if (baseImageFile != null) {
                    String path = baseImageFile.getUrl();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public MiniProgramConfigVO getMiniProgramConfigVO(Long storeUserId) {
        MiniProgramConfigVO miniProgramConfigVO = new MiniProgramConfigVO();
        miniProgramConfigVO.setPath(path+"/storeId="+storeUserId);
        miniProgramConfigVO.setTitle(title);
        miniProgramConfigVO.setUserName(userName);
        StoreUserInfoVO storeUserInfoVO = storeServiceClient.findStoreUserInfo(storeUserId).getData();
        if(storeUserInfoVO != null){
            description = description+storeUserInfoVO.getStoreName();
        }
        miniProgramConfigVO.setDescription(description);
        miniProgramConfigVO.setTransaction(transaction);
        miniProgramConfigVO.setWebPageUrl("http://www.hxd.com");
        return miniProgramConfigVO;
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
