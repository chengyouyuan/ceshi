package com.winhxd.b2c.common.util;

import com.winhxd.b2c.common.domain.common.model.BaseFile;
import com.winhxd.b2c.common.exception.BusinessException;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * @description: 上传文件辅助类
 * @author: zhanglingke
 * @create: 2018-08-06 11:26
 **/
@Component
public class UploadUtil {
    private static final Logger logger = LoggerFactory.getLogger(UploadUtil.class);
    private static final int SUCCESS_CODE = 200;
    private static final int ErrorCode = 1;

    //文件默认保存地址
    @Value("${cdn.picture}")
    private String baseHost = "";

//    @Value("${cdn.dir}")
//    private String dir = "";
//
//    @Value("${cdn.uploadUrl}")
//    private String uploadUrl = "";

    /**
     * 功能描述: 上传文件
     *
     * @auther: zhanglingke
     * @date: 2018-08-17 11:28
     * @param:
     * @return:
     */
    public BaseFile httpClientUploadFile(String fileName, InputStream inputStream) {
        return httpClientUploadFile(baseHost, fileName, inputStream);
    }

    /**
     * 功能描述: 上传文件
     *
     * @auther: zhanglingke
     * @date: 2018-08-17 11:28
     * @param:
     * @return:
     */
    public BaseFile httpClientUploadFile(String host, String fileName, InputStream inputStream) {

        // 第三方服务器请求地址
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String result = "";
        BaseFile baseFile = null;
        int postFixIndex = fileName.lastIndexOf(".");
        //获取后缀
        String postFix = fileName.substring(postFixIndex);
        // 去重
        String uuidName = GeneratePwd.getRandomUUID() + postFix;

        HttpPost httpPost = new HttpPost(host);

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        // 文件流
        builder.addBinaryBody("file", inputStream, ContentType.MULTIPART_FORM_DATA, uuidName);
        // 类似浏览器表单提交，对应input的name和value
        builder.addTextBody("filename", uuidName);
        HttpEntity entity = builder.build();

        httpPost.setEntity(entity);
        HttpResponse response = null;// 执行提交
        try {
            response = httpClient.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 查看是否请求成
        StatusLine statusLine = response.getStatusLine();
        int stateCode = statusLine.getStatusCode();
        // 请求成功
        if (SUCCESS_CODE == stateCode) {
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
                // 将响应内容转换为字符串
                try {
                    result = EntityUtils.toString(responseEntity, Charset.forName("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (result != null) {
                    Map<String, Object> json = JsonUtil.parseJSONObject(result);
                    String path = (String) json.get("path");
                    baseFile = new BaseFile();
                    baseFile.setName(fileName);
                    baseFile.setUrl(path);
                }
            }
        }
        try {
            httpClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baseFile;
    }

}
