package com.winhxd.b2c.pay.service.impl;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.winhxd.b2c.common.cache.Cache;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.CacheName;
import com.winhxd.b2c.common.domain.pay.condition.PayStoreWalletCondition;
import com.winhxd.b2c.common.domain.pay.model.PayStoreWallet;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.pay.dao.PayStoreWalletMapper;
import com.winhxd.b2c.pay.service.PayStoreWalletService;

@Service
public class PayStoreWalletServiceImpl implements PayStoreWalletService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PayFinancialManagerServiceImpl.class);
	@Autowired
	private PayStoreWalletMapper payStoreWalletMapper;
	
	@Resource
	private Cache redisClusterCache;

	@Override
	public int savePayStoreWallet(PayStoreWalletCondition condition) {
		int res = 0;
		if(condition != null){
			// 验证微信绑定入参
			res = valiWeixinCondition(condition);
			if(res == 0){
				// 将其他微信钱包的状态设置为0
				payStoreWalletMapper.updateBatchStatus();
				PayStoreWallet payStoreWallet = new PayStoreWallet();
				BeanUtils.copyProperties(condition, payStoreWallet);
				LOGGER.info("绑定微信支付钱包入参：---"+payStoreWallet);
				//插入当前要绑定的微信钱包信息
				payStoreWallet.setStatus((short)1);
				payStoreWalletMapper.insertSelective(payStoreWallet);
			}
		}else{
			res = BusinessCode.CODE_610030;
		}
		return res;
	}
	
	//验证微信绑定入参
	public int valiWeixinCondition(PayStoreWalletCondition condition){
		int res = 0;
		String mobile = condition.getMobile();
		if(StringUtils.isEmpty(mobile)){
			LOGGER.info("业务异常："+BusinessCode.CODE_610015);
			res = BusinessCode.CODE_610015;
			throw new BusinessException(BusinessCode.CODE_610015);
		}
		String verificationCode = condition.getVerificationCode();
    	if(StringUtils.isEmpty(verificationCode)){
    		LOGGER.info("业务异常："+BusinessCode.CODE_610016);
    		res = BusinessCode.CODE_610016;
    		throw new BusinessException(BusinessCode.CODE_610016);
    	}
    	
		Boolean exists = redisClusterCache.exists(CacheName.PAY_VERIFICATION_CODE+1+"_"+condition.getStoreId());
		System.out.print("微信验证码是否存在-----------"+exists);
		if(exists){
			String code = redisClusterCache.get(CacheName.PAY_VERIFICATION_CODE+1+"_"+condition.getStoreId());
			if(!verificationCode.equals(code)){
				LOGGER.info("业务异常："+BusinessCode.CODE_610019);
				res = BusinessCode.CODE_610019;
				throw new BusinessException(BusinessCode.CODE_610019);
			}
		}else{
			LOGGER.info("业务异常："+BusinessCode.CODE_610020);
			res = BusinessCode.CODE_610020;
			throw new BusinessException(BusinessCode.CODE_610020);
		} 
		String openId = condition.getOpenid();
		if(StringUtils.isEmpty(openId)){
			LOGGER.info("业务异常："+BusinessCode.CODE_610031);
			res = BusinessCode.CODE_610031;
			throw new BusinessException(BusinessCode.CODE_610031);
		}
		return res;
    }
}
