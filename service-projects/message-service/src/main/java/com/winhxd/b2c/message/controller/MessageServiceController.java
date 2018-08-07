package com.winhxd.b2c.message.controller;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.message.condition.NeteaseAccountCondition;
import com.winhxd.b2c.common.domain.message.model.MiniOpenId;
import com.winhxd.b2c.common.domain.message.vo.NeteaseAccountVO;
import com.winhxd.b2c.common.feign.message.MessageServiceClient;
import com.winhxd.b2c.message.service.MiniProgramService;
import com.winhxd.b2c.message.service.SMSService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author jujinbiao
 * @className MessageServiceController
 * @description
 */
public class MessageServiceController implements MessageServiceClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageServiceController.class);

    @Autowired
    SMSService smsService;

    @Autowired
    MiniProgramService miniProgramService;

    @Override
    public ResponseResult<NeteaseAccountVO> getNeteaseAccountInfo(NeteaseAccountCondition neteaseAccountCondition) {
        ResponseResult<NeteaseAccountVO> result = new ResponseResult<>();
        return result;
    }

    @Override
    public ResponseResult<Void> sendSMS(String mobile, String content) {
        ResponseResult<Void> result = new ResponseResult<>();
        try {
            smsService.sendSMS(mobile,content);
        }catch (Exception e){
            LOGGER.error("/message/711/v1/sendSMS,给手机号发短信出错，异常信息为={}",e);
            result.setCode(BusinessCode.CODE_1001);
        }
        return result;
    }

    @Override
    public ResponseResult<MiniOpenId> getMiniOpenId(String code) {
        ResponseResult<MiniOpenId> result = new ResponseResult<>();
        try{
            result = miniProgramService.getMiniOpenId(code);
        }catch (Exception e){
            LOGGER.error("/message/721/v1/getMiniOpenId,根据code获取openid出错，异常信息为={}",e);
            result.setCode(BusinessCode.CODE_1001);
        }
        return result;
    }
}
