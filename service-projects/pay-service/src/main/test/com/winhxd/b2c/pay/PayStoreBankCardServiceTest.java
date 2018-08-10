package com.winhxd.b2c.pay;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.winhxd.b2c.BaseTest;
import com.winhxd.b2c.pay.service.PayStoreBankCardService;

import junit.framework.TestCase;

public class PayStoreBankCardServiceTest extends BaseTest {

	@Autowired
	private PayStoreBankCardService payStoreBankCardService;
	
	@Test
	public void testSave(){
		int result = payStoreBankCardService.saveStoreBankCard(null);
		TestCase.assertTrue(result==1);
	}
}
