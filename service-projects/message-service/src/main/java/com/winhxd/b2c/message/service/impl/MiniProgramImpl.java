package com.winhxd.b2c.message.service.impl;

import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.message.model.MiniOpenId;
import com.winhxd.b2c.message.service.MiniProgramService;
import com.winhxd.b2c.message.utils.OpenIdUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author jujinbiao
 * @className MiniProgramImpl
 * @description
 */
@Service
public class MiniProgramImpl implements MiniProgramService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MiniProgramImpl.class);
    @Autowired
    OpenIdUtil openIdUtil;

    @Override
    public ResponseResult<MiniOpenId> getMiniOpenId(String code) {
        ResponseResult<MiniOpenId> result = new ResponseResult<>();
        MiniOpenId miniOpenId = openIdUtil.oauth2GetOpenid(code);
        result.setData(miniOpenId);
        return result;
    }
}