package com.winhxd.b2c.message.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseResult<NeteaseAccountVO> getNeteaseAccountInfo(@RequestBody NeteaseAccountCondition neteaseAccountCondition) {
        ResponseResult<NeteaseAccountVO> result = new ResponseResult<>();
        try{
            NeteaseAccountVO neteaseAccountInfo = neteaseService.getNeteaseAccountInfo(neteaseAccountCondition);
            result.setData(neteaseAccountInfo);
        }catch (Exception e){
            LOGGER.error("/message/7012/v1/getNeteaseAccountInfo,获取云信用户信息出错，异常信息为={}",e);
            result.setCode(BusinessCode.CODE_1001);
        }
        return result;
    }

    @Override
    public ResponseResult<NeteaseAccountVO> createNeteaseAccount(@RequestBody NeteaseAccountCondition neteaseAccountCondition) {
        ResponseResult<NeteaseAccountVO> result = new ResponseResult<>();
        try{
            NeteaseAccountVO neteaseAccount = neteaseService.createNeteaseAccount(neteaseAccountCondition);
            result.setData(neteaseAccount);
        }catch (Exception e){
            LOGGER.error("/message/7013/v1/createNeteaseAccount,创建云信用户出错，异常信息为={}",e);
            result.setCode(BusinessCode.CODE_1001);
        }
        return result;
    }

    @Override
    public ResponseResult<Void> sendNeteaseMsg(@RequestBody NeteaseMsgCondition neteaseMsgCondition) {
        ResponseResult<Void> result = new ResponseResult<>();
        try{
            neteaseService.sendNeteaseMsg(neteaseMsgCondition);
        }catch (Exception e){
            LOGGER.error("/message/7014/v1/sendNeteaseMsg,给B端用户发云信消息出错，异常信息为={}",e);
        }
        return result;
    }

    @Override
    public ResponseResult<Void> sendSMS(@RequestParam("mobile")String mobile,@RequestParam("content")String content) {
        ResponseResult<Void> result = new ResponseResult<>();
        try {
            smsService.sendSMS(mobile,content);
        }catch (Exception e){
            LOGGER.error("/message/7020/v1/sendSMS,给手机号发短信出错，异常信息为={}",e);
            result.setCode(BusinessCode.CODE_1001);
        }
        return result;
    }

    @Override
    public ResponseResult<MiniOpenId> getMiniOpenId(@RequestParam("code")String code) {
        ResponseResult<MiniOpenId> result = new ResponseResult<>();
        try{
            result = miniProgramService.getMiniOpenId(code);
        }catch (Exception e){
            LOGGER.error("/message/7021/v1/getMiniOpenId,根据code获取openid出错，异常信息为={}",e);
            result.setCode(BusinessCode.CODE_1001);
        }
        return result;
    }

    @Override
    public ResponseResult<Void> sendMiniMsg(@RequestParam("formId")String formId) {
        ResponseResult<Void> result = new ResponseResult<>();
        try {
            result = miniProgramService.sendMiniMsg(formId);
        }catch (Exception e){
            LOGGER.error("/message/7022/v1/sendMsg,给C端用户推送小程序模板消息出错，异常信息为={}",e);
            result.setCode(BusinessCode.CODE_1001);
        }
        return result;
    }
}
