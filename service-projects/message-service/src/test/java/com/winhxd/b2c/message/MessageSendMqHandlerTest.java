package com.winhxd.b2c.message;

import com.winhxd.b2c.common.domain.message.condition.SMSCondition;
import com.winhxd.b2c.message.sms.SmsServerSendUtils;
import com.winhxd.b2c.message.sms.model.SmsSend;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author: FanZhanzhan
 * @Date: 2018-09-03 11:33
 * @Description
 * @Version
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MessageSendMqHandlerTest {

	@Autowired
	private SmsServerSendUtils smsServer;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void sendMiniMsg() {
	}

	@Test
	public void batchSendNeteaseMsg() {
	}

	@Test
	public void sendNeteaseMsg() {
	}

	@Test
	public void sendSms() {
		SMSCondition smsSend = new SMSCondition();
		smsSend.setTelePhoneNo("18513108620");
		smsSend.setContent("测试发送短信验证码");
		smsServer.sendSmsByHxd(smsSend);
	}
}
