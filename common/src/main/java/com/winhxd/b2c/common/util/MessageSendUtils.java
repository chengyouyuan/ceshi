package com.winhxd.b2c.common.util;

import com.winhxd.b2c.common.domain.message.condition.MiniMsgCondition;
import com.winhxd.b2c.common.domain.message.condition.NeteaseMsgCondition;
import com.winhxd.b2c.common.domain.message.condition.NeteaseMsgDelayCondition;
import com.winhxd.b2c.common.domain.message.condition.SMSCondition;
import com.winhxd.b2c.common.mq.MQDestination;
import com.winhxd.b2c.common.mq.StringMessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author jujinbiao
 * @className MessageSendUtils
 * @description 发送消息工具类（云信/短信/小程序模板）
 */
public class MessageSendUtils {
    @Autowired
    private StringMessageSender sender;

    /**
     * 发送短信
     * @param smsCondition
     */
    public void sendSMS(SMSCondition smsCondition){
        sender.send(MQDestination.SMS_MESSAGE,JsonUtil.toJSONString(smsCondition));
    }

    /**
     * 发送云信消息
     * @param neteaseMsgCondition
     */
    public void sendNeteaseMsg(NeteaseMsgCondition neteaseMsgCondition){
        sender.send(MQDestination.NETEASE_MESSAGE,JsonUtil.toJSONString(neteaseMsgCondition));
    }

    /**
     * 发送小程序模板消息
     * @param miniMsgCondition
     */
    public void sendMiniTemplateMsg(MiniMsgCondition miniMsgCondition){
        sender.send(MQDestination.MINI_TEMPLATE_MESSAGE,JsonUtil.toJSONString(miniMsgCondition));
    }

    /**
     * 发送延迟云信消息（后台消息管理，有定时发送云信消息功能）
     */
    public void sendNeteaseMsgDelay(NeteaseMsgDelayCondition neteaseMsgDelayCondition,Integer delayMilliseconds){
        sender.send(MQDestination.NETEASE_MESSAGE_DELAY,JsonUtil.toJSONString(neteaseMsgDelayCondition),delayMilliseconds);
    }

}
