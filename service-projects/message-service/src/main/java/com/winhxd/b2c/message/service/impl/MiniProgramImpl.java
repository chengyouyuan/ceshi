package com.winhxd.b2c.message.service.impl;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.message.condition.WxTemplate;
import com.winhxd.b2c.common.domain.message.model.MiniOpenId;
import com.winhxd.b2c.common.util.JsonUtil;
import com.winhxd.b2c.message.service.MiniProgramService;
import com.winhxd.b2c.message.utils.HttpUtil;
import com.winhxd.b2c.message.utils.MiniProgramUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jujinbiao
 * @className MiniProgramImpl
 * @description
 */
@Service
public class MiniProgramImpl implements MiniProgramService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MiniProgramImpl.class);
    private static final String RETURN_NULL = "null";
    @Autowired
    MiniProgramUtils miniProgramUtils;

    @Override
    public ResponseResult<MiniOpenId> getMiniOpenId(String code) {
        ResponseResult<MiniOpenId> result = new ResponseResult<>();
        if(StringUtils.isNotEmpty(code)){
            MiniOpenId miniOpenId = miniProgramUtils.oauth2GetOpenid(code);
            if (miniOpenId == null || StringUtils.isEmpty(miniOpenId.getOpenId()) || RETURN_NULL.equals(miniOpenId.getOpenId())){
                result.setData(null);
                result.setCode(BusinessCode.CODE_1001);
            }else{
                result.setData(miniOpenId);
            }
        }else{
            LOGGER.info("/message/7021/v1/getMiniOpenId,code is null,then data is null...code={}",code);
            result.setData(null);
            result.setCode(BusinessCode.CODE_1001);
        }
        return result;
    }

    @Override
    public ResponseResult<Void> sendMiniMsg(String formId) {
        String templateUrl = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token=ACCESS_TOKEN";
        String access_token = "12_rozvXnPAm10CMvxMOUWT7Ijc7lHjPfnaM1db2lYiFdJ70VibWNKDO9ROzBuXRyqf_0gzbAHI_2gD-_6I1qyQ92R9eSd_B00lW07zQNzu25qF9IstpehMmNnsKvUKRJhAGASZR";
        templateUrl = templateUrl.replace("ACCESS_TOKEN",access_token);

        WxTemplate template = new WxTemplate();
        template.setTouser("ofTZZ5BX0Heb_77aU6KaiUNWE-QQ");
        template.setTemplate_id("9P9K-qlU8TQPuSDjuYLlNjhd6djZFP8J0uoBQfkAfNs");
        template.setPage("pages/index/index.wxml");
        template.setForm_id(formId);
        Map<String,Object> data = new HashMap<>();

        Map<String,Object> keyword1 = new HashMap();
        keyword1.put("value",new Date());
        data.put("keyword1",keyword1);

        Map<String,Object> keyword2 = new HashMap();
        keyword2.put("value","test名称");
        data.put("keyword2",keyword2);

        Map<String,Object> keyword3 = new HashMap();
        keyword3.put("value","testC287989849384938493");
        data.put("keyword3",keyword3);

        Map<String,Object> keyword4 = new HashMap();
        keyword4.put("value","20元");
        data.put("keyword4",keyword4);

        template.setData(data);

        String templateJson = JsonUtil.toJSONString(template);
        System.out.println("templateJson="+templateJson);
        Map<String, Object> post = HttpUtil.httpRequest(templateUrl, "POST", templateJson);
        System.out.println(post);
        return null;
    }
}
