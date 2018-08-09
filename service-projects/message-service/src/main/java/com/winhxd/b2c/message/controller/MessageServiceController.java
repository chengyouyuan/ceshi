package com.winhxd.b2c.message.controller;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.message.condition.NeteaseAccountCondition;
import com.winhxd.b2c.common.domain.message.condition.NeteaseMsgCondition;
import com.winhxd.b2c.common.domain.message.model.MiniOpenId;
import com.winhxd.b2c.common.domain.message.vo.NeteaseAccountVO;
import com.winhxd.b2c.common.feign.message.MessageServiceClient;
import com.winhxd.b2c.message.service.MiniProgramService;
import com.winhxd.b2c.message.service.NeteaseService;
import com.winhxd.b2c.message.service.SMSService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jujinbiao
 * @className MessageServiceController
 * @description
 */
@RestController
public class MessageServiceController implements MessageServiceClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageServiceController.class);

    @Autowired
    NeteaseService neteaseService;

    @Autowired
    SMSService smsService;

    @Autowired
    MiniProgramService miniProgramService;

    @Override
    public ResponseResult<NeteaseAccountVO> getNeteaseAccountInfo(NeteaseAccountCondition neteaseAccountCondition) {
        ResponseResult<NeteaseAccountVO> result = new ResponseResult<>();
        try{
            NeteaseAccountVO neteaseAccountInfo = neteaseService.getNeteaseAccountInfo(neteaseAccountCondition);
            result.setData(neteaseAccountInfo);
        }catch (Exception e){
            LOGGER.error("/message/701/v1/getNeteaseAccountInfo,获取云信用户信息出错，异常信息为={}",e);
            result.setCode(BusinessCode.CODE_1001);
        }
        return result;
    }

    @Override
    public ResponseResult<NeteaseAccountVO> createNeteaseAccount(NeteaseAccountCondition neteaseAccountCondition) {
        ResponseResult<NeteaseAccountVO> result = new ResponseResult<>();
        try{
            NeteaseAccountVO neteaseAccount = neteaseService.createNeteaseAccount(neteaseAccountCondition);
            result.setData(neteaseAccount);
        }catch (Exception e){
            LOGGER.error("/message/702/v1/createNeteaseAccount,创建云信用户出错，异常信息为={}",e);
            result.setCode(BusinessCode.CODE_1001);
        }
        return result;
    }

    @Override
    public ResponseResult<Void> sendNeteaseMsg(NeteaseMsgCondition neteaseMsgCondition) {
        ResponseResult<Void> result = new ResponseResult<>();
        try{

        }catch (Exception e){
            LOGGER.error("/message/703/v1/sendNeteaseMsg,给B端用户发云信消息出错，异常信息为={}",e);
        }
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
