package com.winhxd.b2c.message.service;

import org.springframework.stereotype.Service;

/**
 * @Description 短信接口
 * @author jujinbiao
 */
public interface SMSService {
    /**
     * 给手机号发送短信
     * @param mobile 手机号
     * @param content 短信内容
     */
    void sendSMS(String mobile,String content);
}
