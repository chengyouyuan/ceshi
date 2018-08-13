package com.winhxd.b2c.pay;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.winhxd.b2c.BaseTest;
import com.winhxd.b2c.common.cache.Cache;
import com.winhxd.b2c.common.constant.CacheName;
import com.winhxd.b2c.common.context.UserContext;
import com.winhxd.b2c.pay.service.PayStoreBankCardService;

import junit.framework.TestCase;

public class PayStoreBankCardServiceTest extends BaseTest {

	@Autowired
	private PayStoreBankCardService payStoreBankCardService;
	
	@Autowired
	private Cache cache;
	
	@Test
	public void testSave(){
		int result = payStoreBankCardService.saveStoreBankCard(null);
		TestCase.assertTrue(result==1);
	}
	
	@Test
	public void testMobile(){
		String modileVerifyCode = cache.get(CacheName.PAY_VERIFICATION_CODE+1);
		System.out.println(modileVerifyCode);
		cache.set(CacheName.PAY_VERIFICATION_CODE+1, "4206");
		cache.expire(CacheName.PAY_VERIFICATION_CODE+1, 60 * 60);
	}
}
