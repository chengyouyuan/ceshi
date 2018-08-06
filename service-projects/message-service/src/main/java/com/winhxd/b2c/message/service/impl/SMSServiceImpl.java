package com.winhxd.b2c.message.service.impl;

import com.winhxd.b2c.message.service.SMSService;
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

    @Override
    public void sendSMS(String mobile,String content) {
        try {
            //sender.send("send_sms_message", value.toJSONString());
        } catch (Exception e) {
            LOGGER.error("发送短信失败", e);
        }
    }
}
