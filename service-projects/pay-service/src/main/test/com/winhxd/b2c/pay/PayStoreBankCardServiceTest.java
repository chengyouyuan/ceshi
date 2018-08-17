package com.winhxd.b2c.pay;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import com.winhxd.b2c.BaseTest;
import com.winhxd.b2c.common.cache.Cache;
import com.winhxd.b2c.common.constant.CacheName;
import com.winhxd.b2c.common.domain.pay.vo.PayFinanceAccountDetailVO;
import com.winhxd.b2c.pay.config.PayWithdrawalConfig;
import com.winhxd.b2c.pay.dao.PayFinanceAccountDetailMapper;
import com.winhxd.b2c.pay.dao.PayWithdrawalsMapper;
import com.winhxd.b2c.pay.service.PayStoreBankCardService;

import junit.framework.TestCase;

public class PayStoreBankCardServiceTest extends BaseTest {

	@Autowired
	private PayStoreBankCardService payStoreBankCardService;
	
	@Resource
	private Cache redisClusterCache;
	
	@Autowired
	private PayFinanceAccountDetailMapper payFinanceAccountDetailMapper;
	
	@Autowired
	private PayWithdrawalsMapper payWithdrawalsMapper;
	
	@Resource
	private PayWithdrawalConfig payWithDrawalConfig;
	
//	@Value("${pay.withdrawal.cmms_amt}")
//	private BigDecimal cmms_amt;// 银行手续费
//	@Value("${pay.withdrawal.rate}")
//	private BigDecimal rate;// 微信费率
//	@Value("${pay.withdrawal.maxmoney}")//最高限额
//	private BigDecimal maxMoney;
	
	@Test
	public void testWithdrawal(){
		System.out.println("cmms_amt-----"+payWithDrawalConfig.getCmmsamt()+"rate-----"+payWithDrawalConfig.getRate()+"maxMoney----"+payWithDrawalConfig.getMaxMoney());
	}
	
	/*@Test
	public void testWithdrawal(){
		System.out.println("cmms_amt-----"+cmms_amt+"rate-----"+rate+"maxMoney----"+maxMoney);
	}*/
	
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
