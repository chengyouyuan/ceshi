package com.winhxd.b2c.message.api;

import com.winhxd.b2c.common.domain.message.condition.NeteaseMsgReadStatusCondition;
import com.winhxd.b2c.message.service.NeteaseService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author: FanZhanzhan
 * @Date: 2018-09-03 16:44
 * @Description
 * @Version
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiNeteaseControllerTest {

	@Autowired
	private NeteaseService neteaseService;

	@Test
	public void findNeteaseMsgBox() {
	}

	@Test
	public void modifyNeteaseMsgReadStatus() {
		NeteaseMsgReadStatusCondition condition = new NeteaseMsgReadStatusCondition();
		condition.setAllRead((short) 1);
		//Long[] ids = {89L, 108L};
		condition.setTimeType((short)0);
//		condition.setMsgIds(ids);
		boolean modifySuccess = neteaseService.modifyNeteaseMsgReadStatus(condition, 17L);
	}
}
