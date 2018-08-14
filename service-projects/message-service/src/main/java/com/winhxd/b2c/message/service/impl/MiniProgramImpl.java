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

        return null;
    }
}
