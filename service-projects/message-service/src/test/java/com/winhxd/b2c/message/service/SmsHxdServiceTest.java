package com.winhxd.b2c.message.service;

import com.winhxd.b2c.common.constant.PayNotifyMsg;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.message.condition.SMSCondition;
import com.winhxd.b2c.common.feign.message.SmsHxdServiceClient;
import com.winhxd.b2c.common.util.GeneratePwd;
import com.winhxd.b2c.message.MessageServiceApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;


@RunWith(SpringRunner.class)
@SpringBootTest
public class SmsHxdServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(SmsHxdServiceTest.class);

    @Autowired
    private SmsHxdServiceClient smsHxdServiceClient;
    
    @Test
    public void sendSmsAsyncByCondition() {
        SMSCondition smsCondition = new SMSCondition();
        String modileVerifyCode = GeneratePwd.generatePwd6Mobile();
        smsCondition.setContent(modileVerifyCode+"（惠小店验证码，您正在进行银行卡绑定，5分钟有效，请勿泄漏给他人）");
        //smsCondition.setContent(PayNotifyMsg.STORE_APPLY_WITHDRWAL);
        smsCondition.setTelePhoneNo("18513108620");
        smsCondition.setType("1");
        ResponseResult result = smsHxdServiceClient.sendSmsAsyncByCondition(smsCondition);
        System.out.println(result.getCode());
    }

}
