package com.winhxd.b2c.pay.service.impl;

import com.winhxd.b2c.common.cache.Cache;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.CacheName;
import com.winhxd.b2c.common.context.StoreUser;
import com.winhxd.b2c.common.context.UserContext;
import com.winhxd.b2c.common.domain.pay.condition.PayStoreWalletCondition;
import com.winhxd.b2c.common.domain.pay.enums.StatusEnums;
import com.winhxd.b2c.common.domain.pay.model.PayStoreWallet;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.pay.dao.PayStoreWalletMapper;
import com.winhxd.b2c.pay.service.PayStoreWalletService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class PayStoreWalletServiceImpl implements PayStoreWalletService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PayFinancialManagerServiceImpl.class);
	@Autowired
	private PayStoreWalletMapper payStoreWalletMapper;
	
	@Resource
	private Cache redisClusterCache;

	@Override
	public void savePayStoreWallet(PayStoreWalletCondition condition) {
        if (condition == null) {
			LOGGER.info("参数为空");
			throw new BusinessException(BusinessCode.CODE_610030);
		}
		// 验证微信绑定入参
		valiWeixinCondition(condition);
		// 将其他微信钱包的状态设置为0
		payStoreWalletMapper.updateBatchStatus(condition.getStoreId());
		PayStoreWallet payStoreWallet = new PayStoreWallet();
		BeanUtils.copyProperties(condition, payStoreWallet);
		LOGGER.info("绑定微信支付钱包入参：---"+payStoreWallet);
		//插入当前要绑定的微信钱包信息
		payStoreWallet.setStatus(StatusEnums.EFFECTIVE.getCode());
		payStoreWallet.setCreated(new Date());
		payStoreWallet.setUpdated(new Date());
		//判断当前的微信账户是否存在
		List<PayStoreWallet> list = payStoreWalletMapper.selectByCondtion(condition);
        if (CollectionUtils.isEmpty(list)) {
			payStoreWalletMapper.insertSelective(payStoreWallet);
        } else {
			PayStoreWallet wallet = list.get(0);
			Long id = wallet.getId();
			payStoreWallet.setId(id);
			payStoreWallet.setUpdated(new Date());
			payStoreWalletMapper.updateByPrimaryKeySelective(payStoreWallet);
		}
	}
	
	//验证微信绑定入参
	public void valiWeixinCondition(PayStoreWalletCondition condition){
		String mobile = condition.getMobile();
		if(StringUtils.isEmpty(mobile)){
			LOGGER.info("业务异常："+BusinessCode.CODE_610015);
			throw new BusinessException(BusinessCode.CODE_610015);
		}
		String verificationCode = condition.getVerificationCode();
    	if(StringUtils.isEmpty(verificationCode)){
    		LOGGER.info("业务异常："+BusinessCode.CODE_610016);
    		throw new BusinessException(BusinessCode.CODE_610016);
    	}
    	
    	StoreUser currentStoreUser = UserContext.getCurrentStoreUser();
    ///////////////////测试假数据///////////////////////
//    	StoreUser currentStoreUser = new StoreUser();
//    	currentStoreUser.setBusinessId(84l);
   ////////////////////////////////////////////////////
    	
		Boolean exists = redisClusterCache.exists(CacheName.PAY_VERIFICATION_CODE+1+"_"+currentStoreUser.getBusinessId());
		LOGGER.info("微信验证码是否存在-----------" + exists);
		if(!exists){
			LOGGER.info("业务异常："+BusinessCode.CODE_610020);
			throw new BusinessException(BusinessCode.CODE_610020);
		}
		String code = redisClusterCache.get(CacheName.PAY_VERIFICATION_CODE+1+"_"+currentStoreUser.getBusinessId());
		if(!verificationCode.equals(code)){
			LOGGER.info("业务异常："+BusinessCode.CODE_610019);
			throw new BusinessException(BusinessCode.CODE_610019);
		}
		
		String openId = condition.getOpenid();
		if(StringUtils.isEmpty(openId)){
			LOGGER.info("业务异常："+BusinessCode.CODE_610031);
			throw new BusinessException(BusinessCode.CODE_610031);
		}
    }
}
