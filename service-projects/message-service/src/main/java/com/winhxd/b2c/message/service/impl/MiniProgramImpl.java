package com.winhxd.b2c.message.service.impl;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.context.CustomerUser;
import com.winhxd.b2c.common.context.UserContext;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.message.condition.MiniFormIdCondition;
import com.winhxd.b2c.common.domain.message.condition.MiniMsgCondition;
import com.winhxd.b2c.common.domain.message.condition.MiniTemplateData;
import com.winhxd.b2c.common.domain.message.enums.MiniMsgTypeEnum;
import com.winhxd.b2c.common.domain.message.model.MessageCustomerFormIds;
import com.winhxd.b2c.common.domain.message.model.MessageMiniTemplate;
import com.winhxd.b2c.common.domain.message.model.MessageWechatHistory;
import com.winhxd.b2c.common.domain.message.model.MiniOpenId;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.mq.MQHandler;
import com.winhxd.b2c.common.mq.StringMessageListener;
import com.winhxd.b2c.common.util.JsonUtil;
import com.winhxd.b2c.message.dao.MessageCustomerFormIdsMapper;
import com.winhxd.b2c.message.dao.MessageWechatHistoryMapper;
import com.winhxd.b2c.message.service.MiniProgramService;
import com.winhxd.b2c.message.utils.MiniProgramUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author jujinbiao
 * @className MiniProgramImpl
 * @description
 */
@Service
public class MiniProgramImpl implements MiniProgramService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MiniProgramImpl.class);
    private static final String RETURN_NULL = "null";
    private static final String RETURN_ERR_CODE = "errcode";
    private static final String RETURN_ERR_MSG = "errmsg";
    private static final String SUCCESS_CODE = "0";
    /**错误码，formid不正确，或者过期或已被使用*/
    private static final String[] ERR_CODE = {"41028","41029"};
    @Autowired
    MiniProgramUtils miniProgramUtils;
    @Autowired
    MessageCustomerFormIdsMapper customerFormIdsMapper;
    @Autowired
    MessageWechatHistoryMapper messageWechatHistoryMapper;

    @Override
    public ResponseResult<MiniOpenId> getMiniOpenId(String code) {
        ResponseResult<MiniOpenId> result = new ResponseResult<>();
        if(StringUtils.isNotEmpty(code)){
            //根据code获取openid信息
            MiniOpenId miniOpenId = miniProgramUtils.oauth2GetOpenid(code);
            if (miniOpenId == null || StringUtils.isEmpty(miniOpenId.getOpenid()) || RETURN_NULL.equals(miniOpenId.getOpenid())){
                result.setData(null);
                result.setCode(BusinessCode.CODE_702101);
            }else{
                result.setData(miniOpenId);
            }
        }else{
            LOGGER.error("MiniProgramImpl -> getMiniOpenId,根据code获取小程序的openid和session_key出错，code is null，code={}",code);
            result.setData(null);
            result.setCode(BusinessCode.CODE_702102);
        }
        return result;
    }

    @StringMessageListener(MQHandler.MINI_TEMPLATE_MESSAGE_HANDLER)
    public void sendMiniMsg(String miniMsgConditionJson) {
        LOGGER.info("消息服务->发送小程序模板消息，MiniProgramImpl.sendMiniMsg(),miniMsgConditionJson={}",miniMsgConditionJson);
        MiniMsgCondition miniMsgCondition = JsonUtil.parseJSONObject(miniMsgConditionJson,MiniMsgCondition.class);
        if(StringUtils.isEmpty(miniMsgCondition.getToUser())){
            LOGGER.error("MiniProgramImpl -> sendMiniMsg,给C端用户推送小程序模板消息，toUser为空");
            throw new BusinessException(BusinessCode.CODE_702201);
        }
        List<MiniTemplateData> params = miniMsgCondition.getData();
        if (CollectionUtils.isEmpty(params)){
            LOGGER.error("MiniProgramImpl -> sendMiniMsg,给C端用户推送小程序模板消息，消息内容为空");
            throw new BusinessException(BusinessCode.CODE_702202);
        }
        //根据小程序模板，给C端用户发消息
        //根据消息类型获取模板id，模板内容
        MiniMsgTypeEnum msgTypeEnum = MiniMsgTypeEnum.getMiniMsgTypeEnumByMsgType(miniMsgCondition.getMsgType());
        if (msgTypeEnum == null){
            LOGGER.error("MiniProgramImpl -> sendMiniMsg,给C端用户推送小程序模板消息，msgTypeEnum不存在，msgType={}",miniMsgCondition.getMsgType());
            throw new BusinessException(BusinessCode.CODE_702203);
        }
        //消息模板id
        String templateId = msgTypeEnum.getTemplateId();
        //根据toUser获取可用的formid，若无可用formid,返回错误码
        MessageCustomerFormIds formIdByOpenid = customerFormIdsMapper.getProd(miniMsgCondition.getToUser());
        if (formIdByOpenid == null){
            LOGGER.error("MiniProgramImpl -> sendMiniMsg,给C端用户推送小程序模板消息，不存在可用的formid，toUser={}",miniMsgCondition.getToUser());
            throw new BusinessException(BusinessCode.CODE_702204);
        }
        String formId = formIdByOpenid.getFormid();
        //组织参数，发送消息
        MessageMiniTemplate template = prepareMiniMsg(miniMsgCondition,templateId,formId);
        try {
            String returnStr = miniProgramUtils.sendMiniMsg(JsonUtil.toJSONString(template));
            if (StringUtils.isEmpty(returnStr)){
                LOGGER.error("MiniProgramImpl -> sendMiniMsg,给C端用户推送小程序模板消息出错,发送后没有返回returnStr。");
                throw new BusinessException(BusinessCode.CODE_702205);
            }
            Map<String, Object> stringObjectMap = JsonUtil.parseJSONObject(returnStr);
            if(stringObjectMap.get(RETURN_ERR_CODE) == null){
                LOGGER.error("MiniProgramImpl -> sendMiniMsg,给C端用户推送小程序模板消息，发送消息出错,没有返回错误码。");
                throw new BusinessException(BusinessCode.CODE_702206);
            }
            String errCode = String.valueOf(stringObjectMap.get(RETURN_ERR_CODE));
            String errMsg = String.valueOf(stringObjectMap.get(RETURN_ERR_MSG));
            List<String> strings = Arrays.asList(ERR_CODE);
            if (SUCCESS_CODE.equals(errCode)){
                //发送成功，保存消息发送记录，删除formid记录
                saveMiniMsgHistory(template);
                customerFormIdsMapper.deleteByPrimaryKey(formIdByOpenid.getId());
            }else if (strings.contains(errCode)){
                //formid不正确，或者过期或已被使用，删除formid记录
                customerFormIdsMapper.deleteByPrimaryKey(formIdByOpenid.getId());
            }else{
                LOGGER.error("MiniProgramImpl -> ,给C端用户推送小程序模板消息，发送消息出错,错误信息为={}",errMsg);
                throw new BusinessException(BusinessCode.CODE_702207);
            }
        } catch (Exception e) {
            LOGGER.error("MiniProgramImpl -> sendMiniMsg,给C端用户推送小程序模板消息，发送消息出错",e);
            throw new BusinessException(BusinessCode.CODE_702207);
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
        return template;
    }

    @Override
    public void saveFormIds(MiniFormIdCondition miniFormIdCondition) {
        CustomerUser user = UserContext.getCurrentCustomerUser();
        if (user == null || user.getCustomerId() == null || StringUtils.isEmpty(user.getOpenid())){
            LOGGER.error("MiniProgramImpl ->saveFormIds,保存formid出错，用户凭证无效");
            throw new BusinessException(BusinessCode.CODE_1002);
        }
        if (CollectionUtils.isEmpty(miniFormIdCondition.getFormIds())){
            LOGGER.error("MiniProgramImpl ->saveFormIds,保存formid出错，formids为空");
            throw new BusinessException(BusinessCode.CODE_702301);
        }
        //批量保存C端用户formid
        List<MessageCustomerFormIds> list = new ArrayList<>();
        for (String formid:miniFormIdCondition.getFormIds()) {
            MessageCustomerFormIds customerFormIds = new MessageCustomerFormIds();
            customerFormIds.setOpenid(user.getOpenid());
            customerFormIds.setFormid(formid);
            customerFormIds.setCreated(new Date());
            list.add(customerFormIds);
        }
        if (CollectionUtils.isNotEmpty(list)){
            customerFormIdsMapper.insertFormIds(list);
        }
    }

}
