package com.winhxd.b2c.pay.service.impl;

import java.util.Date;
import java.util.List;

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
import com.winhxd.b2c.common.context.StoreUser;
import com.winhxd.b2c.common.context.UserContext;
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
				payStoreWalletMapper.updateBatchStatus(condition.getStoreId());
				PayStoreWallet payStoreWallet = new PayStoreWallet();
				BeanUtils.copyProperties(condition, payStoreWallet);
				LOGGER.info("绑定微信支付钱包入参：---"+payStoreWallet);
				//插入当前要绑定的微信钱包信息
				payStoreWallet.setStatus((short)1);
				payStoreWallet.setCreated(new Date());
				//判断当前的微信账户是否存在
				List<PayStoreWallet> list = payStoreWalletMapper.selectByCondtion(condition);
				if(list.size()>0){
					if(list.size() > 1){
						LOGGER.info("当前微信账户有多个重复，请联系管理员");
						res = BusinessCode.CODE_610021;
						throw new BusinessException(res);
					}
					PayStoreWallet wallet = list.get(0);
					Long id = wallet.getId();
					payStoreWallet.setId(id);
					payStoreWalletMapper.updateByPrimaryKeySelective(payStoreWallet);
				}else{
					payStoreWalletMapper.insertSelective(payStoreWallet);
				}
			}
		}else{
			res = BusinessCode.CODE_610030;
			throw new BusinessException(res);
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
    	
    	StoreUser currentStoreUser = UserContext.getCurrentStoreUser();
    ///////////////////测试假数据///////////////////////
//    	StoreUser currentStoreUser = new StoreUser();
//    	currentStoreUser.setBusinessId(106l);
   ////////////////////////////////////////////////////
    	
		Boolean exists = redisClusterCache.exists(CacheName.PAY_VERIFICATION_CODE+1+"_"+currentStoreUser.getBusinessId());
		System.out.print("微信验证码是否存在-----------"+exists);
		if(exists){
			String code = redisClusterCache.get(CacheName.PAY_VERIFICATION_CODE+1+"_"+currentStoreUser.getBusinessId());
			if(!verificationCode.equals(code)){
				LOGGER.info("业务异常："+BusinessCode.CODE_610019);
				res = BusinessCode.CODE_610019;
				throw new BusinessException(BusinessCode.CODE_610019);
			}
		}else{
			LOGGER.info("业务异常："+BusinessCode.CODE_610016);
			res = BusinessCode.CODE_610016;
			throw new BusinessException(BusinessCode.CODE_610016);
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
