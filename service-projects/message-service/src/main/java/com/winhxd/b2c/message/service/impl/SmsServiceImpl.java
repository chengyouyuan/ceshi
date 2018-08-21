package com.winhxd.b2c.message.service.impl;

import com.winhxd.b2c.common.domain.message.condition.SMSCondition;
import com.winhxd.b2c.common.mq.MQHandler;
import com.winhxd.b2c.common.mq.StringMessageListener;
import com.winhxd.b2c.common.util.JsonUtil;
import com.winhxd.b2c.message.sms.SmsServerSendUtils;
import com.winhxd.b2c.message.sms.model.SmsSend;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author jujinbiao
 * @className SmsServiceImpl
 * @description
 */
@Service
public class SmsServiceImpl {
	private static final Logger LOGGER = LoggerFactory.getLogger(SmsServiceImpl.class);

	@Autowired
	private SmsServerSendUtils smsServer;

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
			LOGGER.error("发送短信失败", e);
		}
	}
}
