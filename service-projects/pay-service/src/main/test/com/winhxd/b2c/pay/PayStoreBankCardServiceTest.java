package com.winhxd.b2c.pay;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.winhxd.b2c.BaseTest;
import com.winhxd.b2c.common.cache.Cache;
import com.winhxd.b2c.common.constant.CacheName;
import com.winhxd.b2c.common.domain.pay.vo.PayFinanceAccountDetailVO;
import com.winhxd.b2c.pay.dao.PayFinanceAccountDetailMapper;
import com.winhxd.b2c.pay.service.PayStoreBankCardService;

import junit.framework.TestCase;

public class PayStoreBankCardServiceTest extends BaseTest {

	@Autowired
	private PayStoreBankCardService payStoreBankCardService;
	
	@Resource
	private Cache redisClusterCache;
	
	@Autowired
	private PayFinanceAccountDetailMapper payFinanceAccountDetailMapper;
	
	@Test
	public void testquery(){
		//查询总出账和总入账金额
		List<PayFinanceAccountDetailVO> toalInoutMoney = payFinanceAccountDetailMapper.selectTotalInOutMoney(2l);
		System.out.print(toalInoutMoney);
	}
	
	@Test
	public void testSave(){
		int result = payStoreBankCardService.saveStoreBankCard(null);
		TestCase.assertTrue(result==1);
	}
	
	@Test
	public void testMobile(){
		String modileVerifyCode = redisClusterCache.get(CacheName.PAY_VERIFICATION_CODE+1);
		System.out.println(modileVerifyCode);
		redisClusterCache.set(CacheName.PAY_VERIFICATION_CODE+1, "4206");
		redisClusterCache.expire(CacheName.PAY_VERIFICATION_CODE+1, 60 * 60);
	}
}
