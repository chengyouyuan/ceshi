package com.winhxd.b2c.message.service.impl;

import com.winhxd.b2c.common.util.JsonUtil;
import com.winhxd.b2c.message.service.SMSService;
import com.winhxd.b2c.message.sms.SmsServerSendUtils;
import com.winhxd.b2c.message.sms.model.SmsSend;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author jujinbiao
 * @className SMSServiceImpl
 * @description
 */
@Service
public class SMSServiceImpl implements SMSService {
	private static final Logger LOGGER = LoggerFactory.getLogger(SMSServiceImpl.class);

	@Autowired
	private SmsServerSendUtils smsServer;

	@Override
	public void sendSMS(String mobile, String content) {
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
