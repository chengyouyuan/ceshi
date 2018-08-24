package com.winhxd.b2c.message.service.impl;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.message.condition.*;
import com.winhxd.b2c.common.domain.message.enums.MiniMsgTypeEnum;
import com.winhxd.b2c.common.domain.message.enums.MsgCategoryEnum;
import com.winhxd.b2c.common.domain.message.enums.MsgPageTypeEnum;
import com.winhxd.b2c.common.domain.message.model.*;
import com.winhxd.b2c.common.mq.MQHandler;
import com.winhxd.b2c.common.mq.StringMessageListener;
import com.winhxd.b2c.common.util.JsonUtil;
import com.winhxd.b2c.message.dao.MessageCustomerFormIdsMapper;
import com.winhxd.b2c.message.dao.MessageNeteaseAccountMapper;
import com.winhxd.b2c.message.dao.MessageNeteaseHistoryMapper;
import com.winhxd.b2c.message.dao.MessageWechatHistoryMapper;
import com.winhxd.b2c.message.sms.SmsServerSendUtils;
import com.winhxd.b2c.message.sms.model.SmsSend;
import com.winhxd.b2c.message.utils.MiniProgramUtils;
import com.winhxd.b2c.message.utils.NeteaseUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.*;

/**
 * @author jujinbiao
 * @className MessageSendMqHandler 消息服务MQ消费
 * @description
 */

public class MessageSendMqHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageSendMqHandler.class);
    private static final String ACCID_ADMIN = "admin";
    private static final String RETURN_ERR_CODE = "errcode";
    private static final String RETURN_ERR_MSG = "errmsg";
    private static final String SUCCESS_CODE_0 = "0";
    private static final String SUCCESS_CODE_200 = "200";
    private static final String PARAM_CODE = "code";
    private static final String PARAM_MSGID = "data.msgid";
    /**错误码，formid不正确，或者过期或已被使用*/
    private static final String[] ERR_CODE = {"41028","41029"};

    @Autowired
    MessageCustomerFormIdsMapper customerFormIdsMapper;

    @Autowired
    MiniProgramUtils miniProgramUtils;

    @Autowired
    MessageWechatHistoryMapper messageWechatHistoryMapper;

    @Autowired
    NeteaseUtils neteaseUtils;

    @Autowired
    MessageNeteaseHistoryMapper messageNeteaseHistoryMapper;

    @Autowired
    MessageNeteaseAccountMapper neteaseAccountMapper;

    @Autowired
    private SmsServerSendUtils smsServer;

    @Value("${wechat.miniProgram.msgTemplate.storeConfirmOrder}")
    private String storeConfirmOrderTid;

    @Value("${wechat.miniProgram.msgTemplate.orderFinish}")
    private String orderFinishTid;

    @Value("${wechat.miniProgram.msgTemplate.orderCanceled}")
    private String orderCanceledTid;

    @Value("${wechat.miniProgram.msgTemplate.paySuccess}")
    private String paySuccessTid;

    @Value("${wechat.miniProgram.msgTemplate.refundSuccess}")
    private String refundSuccessTid;

    /**
     * 发送小程序消息MQ消费
     * @param miniMsgConditionJson
     */
    @StringMessageListener(MQHandler.MINI_TEMPLATE_MESSAGE_HANDLER)
    public void sendMiniMsg(String miniMsgConditionJson) {
        LOGGER.info("消息服务->发送小程序模板消息，MiniProgramImpl.sendMiniMsg(),miniMsgConditionJson={}",miniMsgConditionJson);
        MiniMsgCondition miniMsgCondition = JsonUtil.parseJSONObject(miniMsgConditionJson,MiniMsgCondition.class);
        if(StringUtils.isEmpty(miniMsgCondition.getToUser())){
            LOGGER.error("MiniProgramImpl -> sendMiniMsg,给C端用户推送小程序模板消息，toUser为空,miniMsgConditionJson={}",miniMsgConditionJson);
            return;
        }
        List<MiniTemplateData> params = miniMsgCondition.getData();
        if (CollectionUtils.isEmpty(params)){
            LOGGER.error("MiniProgramImpl -> sendMiniMsg,给C端用户推送小程序模板消息，消息内容为空,miniMsgConditionJson={}",miniMsgConditionJson);
            return;
        }
        //根据小程序模板，给C端用户发消息
        //根据消息类型获取模板id，模板内容
        MiniMsgTypeEnum msgTypeEnum = MiniMsgTypeEnum.getMiniMsgTypeEnumByMsgType(miniMsgCondition.getMsgType());
        if (msgTypeEnum == null){
            LOGGER.error("MiniProgramImpl -> sendMiniMsg,给C端用户推送小程序模板消息，msgTypeEnum不存在，msgType={}",miniMsgCondition.getMsgType());
            return;
        }
        //消息模板id
        String templateId = getTemplateId(msgTypeEnum.getMsgType());
        //根据toUser获取可用的formid，若无可用formid,返回错误码
        MessageCustomerFormIds formIdByOpenid = customerFormIdsMapper.getProd(miniMsgCondition.getToUser());
        if (formIdByOpenid == null){
            LOGGER.error("MiniProgramImpl -> sendMiniMsg,给C端用户推送小程序模板消息，不存在可用的formid，toUser={}",miniMsgCondition.getToUser());
            return;
        }
        String formId = formIdByOpenid.getFormid();
        //组织参数，发送消息
        MessageMiniTemplate template = prepareMiniMsg(miniMsgCondition,templateId,formId);
        try {
            String returnStr = miniProgramUtils.sendMiniMsg(JsonUtil.toJSONString(template));
            if (StringUtils.isEmpty(returnStr)){
                LOGGER.error("MiniProgramImpl -> sendMiniMsg,给C端用户推送小程序模板消息出错,发送后没有返回returnStr,miniMsgConditionJson={}",miniMsgConditionJson);
                return;
            }
            Map<String, Object> stringObjectMap = JsonUtil.parseJSONObject(returnStr);
            if(stringObjectMap.get(RETURN_ERR_CODE) == null){
                LOGGER.error("MiniProgramImpl -> sendMiniMsg,给C端用户推送小程序模板消息，发送消息出错,没有返回错误码,miniMsgConditionJson={}",miniMsgConditionJson);
                return;
            }
            String errCode = String.valueOf(stringObjectMap.get(RETURN_ERR_CODE));
            String errMsg = String.valueOf(stringObjectMap.get(RETURN_ERR_MSG));
            List<String> strings = Arrays.asList(ERR_CODE);
            if (SUCCESS_CODE_0.equals(errCode)){
                //发送成功，保存消息发送记录，删除formid记录
                saveMiniMsgHistory(template);
                customerFormIdsMapper.deleteByPrimaryKey(formIdByOpenid.getId());
            }else if (strings.contains(errCode)){
                //formid不正确，或者过期或已被使用，删除formid记录
                customerFormIdsMapper.deleteByPrimaryKey(formIdByOpenid.getId());
            }else{
                LOGGER.error("MiniProgramImpl -> ,给C端用户推送小程序模板消息，发送消息出错,miniMsgConditionJson={}",miniMsgConditionJson);
                LOGGER.error("MiniProgramImpl -> ,给C端用户推送小程序模板消息，发送消息出错,错误信息为={}",errMsg);
            }
        } catch (Exception e) {
            LOGGER.error("MiniProgramImpl -> sendMiniMsg,给C端用户推送小程序模板消息，发送消息出错,miniMsgConditionJson={}",miniMsgConditionJson);
            LOGGER.error("MiniProgramImpl -> sendMiniMsg,给C端用户推送小程序模板消息，发送消息出错",e);
        }
    }

    /**
     * 批量发送云信消息MQ消费
     * @param neteaseMsgDelayConditionJson
     */
    @StringMessageListener(MQHandler.NETEASE_MESSAGE_DELAY_HANDLER)
    public void batchSendNeteaseMsg(String neteaseMsgDelayConditionJson){
        LOGGER.info("消息服务->批量发送云信消息，MessageBatchPushServiceImpl.batchSendNeteaseMsg(),neteaseMsgDelayConditionJson={}",neteaseMsgDelayConditionJson);
        NeteaseMsgDelayCondition neteaseMsgDelayCondition = JsonUtil.parseJSONObject(neteaseMsgDelayConditionJson,NeteaseMsgDelayCondition.class);
        List<String> accids = neteaseMsgDelayCondition.getAccids();
        String msgContent = neteaseMsgDelayCondition.getMsgContent();
        String[] accidsArr = accids.toArray(new String[accids.size()]);
        //给所有门店批量推送云信消息
        Map<String, Object> msgMap = neteaseUtils.sendTxtMessage2Batch(accidsArr,msgContent);
        if (SUCCESS_CODE_200.equals(String.valueOf(msgMap.get(PARAM_CODE)))) {
            //云信消息发送成功
            saveBatchNeteaseMsgHistory(accids, msgContent);
        } else {
            LOGGER.error("MessageBatchPushServiceImpl ->batchPushMessage,给B端门店手动推送云信消息出错，neteaseMsgDelayConditionJson={}", neteaseMsgDelayConditionJson);
            LOGGER.error("MessageBatchPushServiceImpl ->batchPushMessage,给B端门店手动推送云信消息出错，错误码={}", String.valueOf(msgMap.get(PARAM_CODE)));
        }
    }

    /**
     * 发送云信消息MQ消费
     * @param neteaseMsgConditionJson
     */
    @StringMessageListener(value = MQHandler.NETEASE_MESSAGE_HANDLER)
    public void sendNeteaseMsg(String neteaseMsgConditionJson) {
        NeteaseMsgCondition neteaseMsgCondition = JsonUtil.parseJSONObject(neteaseMsgConditionJson,NeteaseMsgCondition.class);
        LOGGER.info("消息服务->发送云信消息，MessageSendMqHandler.sendNeteaseMsg(),neteaseMsgConditionJson={}",neteaseMsgConditionJson);
        //校验参数
        int errorCode = verifyParamSend(neteaseMsgCondition);
        if (BusinessCode.CODE_OK != errorCode) {
            LOGGER.error("消息服务 ->发送云信消息异常，MessageSendMqHandler.sendNeteaseMsg(),参数错误，errorCode={}",errorCode);
            return;
        }
        MessageNeteaseAccount account = neteaseAccountMapper.getNeteaseAccountByCustomerId(neteaseMsgCondition.getCustomerId());
        if (account == null) {
            //云信用户不存在
            LOGGER.error("消息服务 ->发送云信消息异常，MessageSendMqHandler.sendNeteaseMsg(),参数错误，云信用户不存在,customerId={}",neteaseMsgCondition.getCustomerId());
            return;
        }
        int msgType = neteaseMsgCondition.getNeteaseMsg().getMsgType();
        //在消息盒子中，则保存消息记录
        String msgId = "";
        if (msgType == 0){
            MessageNeteaseHistory history = saveNeteaseMsgHistory(account.getAccid(), neteaseMsgCondition.getNeteaseMsg());
            msgId = String.valueOf(history.getId());
        }
        //发送云信消息
        Map<String, Object> msgMap = neteaseUtils.sendTxtMessage2Person(account.getAccid(), neteaseMsgCondition,msgId);
        if (SUCCESS_CODE_200.equals(String.valueOf(msgMap.get(PARAM_CODE)))) {
            //云信消息发送成功
            LOGGER.info("NeteaseServiceImpl ->sendNeteaseMsg,给B端用户发云信消息成功 msg_id_server={}",String.valueOf(msgMap.get(PARAM_MSGID)));
        } else {
            LOGGER.error("NeteaseServiceImpl ->sendNeteaseMsg,给B端用户发云信消息出错 neteaseMsgCondition={}", neteaseMsgCondition.getCustomerId() + "," + neteaseMsgCondition.getNeteaseMsg().getMsgContent());
        }
    }

    /**
     * 发送短信MQ消费
     * @param smsConditionJson
     */
    @StringMessageListener(value = MQHandler.SMS_MESSAGE_HANDLER)
    public void sendSms(String smsConditionJson) {
        LOGGER.info("消息服务->发送短信，SmsServiceImpl.sendSms(),smsConditionJson={}",smsConditionJson);
        SMSCondition smsCondition = JsonUtil.parseJSONObject(smsConditionJson,SMSCondition.class);
        String mobile = smsCondition.getMobile();
        String content = smsCondition.getContent();
        try {
            SmsSend smsSend = new SmsSend();
            smsSend.setTelePhoneNo(mobile);
            smsSend.setContent(content);
            smsServer.sendSms(smsSend);
        } catch (Exception e) {
            LOGGER.error("消息服务->发送短信失败，SmsServiceImpl.sendSms(),smsConditionJson={}",smsConditionJson);
            LOGGER.error("发送短信失败", e);
        }
    }

    /**
     * 保存小程序模板消息发送记录
     * @param template
     */
    private void saveMiniMsgHistory(MessageMiniTemplate template) {
        MessageWechatHistory messageWechatHistory = new MessageWechatHistory();
        messageWechatHistory.setToUser(template.getTouser());
        messageWechatHistory.setTemplateId(template.getTemplate_id());
        messageWechatHistory.setPage(template.getPage());
        messageWechatHistory.setFormId(template.getForm_id());
        messageWechatHistory.setData(JsonUtil.toJSONString(template.getData()));
        messageWechatHistory.setSendTime(new Date());
        messageWechatHistoryMapper.insert(messageWechatHistory);
    }

    /**
     * 组织发送小程序模板消息参数
     * @param miniMsgCondition
     * @param templateId
     * @param formId
     * @return
     */
    private MessageMiniTemplate prepareMiniMsg(MiniMsgCondition miniMsgCondition, String templateId, String formId) {
        MessageMiniTemplate template = new MessageMiniTemplate();
        template.setTouser(miniMsgCondition.getToUser());
        template.setTemplate_id(templateId);
        template.setPage(miniMsgCondition.getPage());
        template.setForm_id(formId);
        Map<String,Object> data = new HashMap<>();
        List<MiniTemplateData> templateDatas = miniMsgCondition.getData();
        for (MiniTemplateData templateData:templateDatas) {
            Map<String,Object> value = new HashMap();
            value.put("value",templateData.getValue());
            data.put(templateData.getKeyName(),value);
        }
        template.setData(data);
        if(StringUtils.isNotEmpty(miniMsgCondition.getEmphasisKeyword())){
            template.setEmphasis_keyword(miniMsgCondition.getEmphasisKeyword());
        }
        return template;
    }
    /**
     * 保存云信消息推送记录
     * @param accids
     * @param msgContent
     */
    private void saveBatchNeteaseMsgHistory(List<String> accids, String msgContent) {
        List<MessageNeteaseHistory> list = new ArrayList<>();
        for (String accid: accids) {
            MessageNeteaseHistory history = new MessageNeteaseHistory();
            history.setFromAccid(ACCID_ADMIN);
            history.setToAccid(accid);
            //消息类型0：text
            history.setMsgType(Short.valueOf("0"));
            history.setMsgBody(msgContent);
            history.setMsgTimeStamp(new Date());
            //页面跳转类型：无跳转
            history.setPageType(MsgPageTypeEnum.NOTICE.getPageType());
            history.setTreeCode(null);
            history.setMsgCategory(MsgCategoryEnum.HUI_NOTICE.getTypeCode());
            //1：未读
            history.setReadStatus("1");
            list.add(history);
        }
        messageNeteaseHistoryMapper.insertHistories(list);
    }

    /**
     * 保存云信消息发送记录
     *
     * @param accid
     * @param neteaseMsg
     */
    private MessageNeteaseHistory saveNeteaseMsgHistory(String accid, NeteaseMsg neteaseMsg) {
        MessageNeteaseHistory history = new MessageNeteaseHistory();
        history.setFromAccid(ACCID_ADMIN);
        history.setToAccid(accid);
        history.setMsgType(Short.valueOf("0"));
        history.setMsgBody(neteaseMsg.getMsgContent());
        history.setExtJson(NeteaseUtils.buildExtJsonMsg4Save(neteaseMsg));
        history.setPageType(neteaseMsg.getPageType());
        history.setTreeCode(neteaseMsg.getTreeCode());
        history.setMsgCategory(neteaseMsg.getMsgCategory());
        history.setMsgTimeStamp(new Date());
        messageNeteaseHistoryMapper.insert(history);
        return history;
    }

    /**
     * 校验发送云信消息参数
     *
     * @param netEaseCondition
     */
    private int verifyParamSend(NeteaseMsgCondition netEaseCondition) {
        if (netEaseCondition.getCustomerId() == null) {
            LOGGER.info("给B端用户发送云信消息，接口参数getCustomerId为空");
            return BusinessCode.CODE_701401;
        }
        if (netEaseCondition.getNeteaseMsg() == null) {
            LOGGER.info("给B端用户发送云信消息，,接口参数getEaseMsg为空");
            return BusinessCode.CODE_701402;
        }
        return BusinessCode.CODE_OK;
    }

    /**
     * 根据消息类型获取模板id
     * @param msgType
     * @return
     */
    private String getTemplateId(short msgType) {
        String templateId;
        switch (msgType){
            case 1:
                templateId = storeConfirmOrderTid;
                break;
            case 2:
                templateId = orderFinishTid;
                break;
            case 3:
                templateId = orderCanceledTid;
                break;
            case 4:
                templateId = paySuccessTid;
                break;
            case 5:
                templateId = refundSuccessTid;
                break;
            default:
                templateId = "";
        }
        return templateId;
    }
}
