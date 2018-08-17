package com.winhxd.b2c.message.service.impl;

import com.winhxd.b2c.message.service.SMSService;
import com.winhxd.b2c.message.sms.SmsServerSendUtils;
import org.json.simple.JSONObject;
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
			JSONObject value = new JSONObject();
			value.put("telePhoneNo", mobile);
			value.put("content", content);
//            if (StringUtils.isNotBlank(username)) {
//                value.put("username", username);
//            }
//            if (StringUtils.isNotBlank(type)) {
//                value.put("type", type);
//            }
//            if (StringUtils.isNotBlank(grp)) {
//                value.put("grp", grp);
//            }"send_sms_message",
			smsServer.sendSms(value.toJSONString());
		} catch (Exception e) {
			LOGGER.error("发送短信失败", e);
		}
	}
}
