package com.winhxd.b2c.pay.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.winhxd.b2c.common.cache.Cache;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.CacheName;
import com.winhxd.b2c.common.context.StoreUser;
import com.winhxd.b2c.common.context.UserContext;
import com.winhxd.b2c.common.domain.pay.condition.StoreBankCardCondition;
import com.winhxd.b2c.common.domain.pay.model.StoreBankCard;
import com.winhxd.b2c.common.domain.pay.vo.StoreBankCardVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.pay.api.ApiPayStoreBindBankCardController;
import com.winhxd.b2c.pay.dao.StoreBankCardMapper;
import com.winhxd.b2c.pay.service.PayStoreBankCardService;

@Service
public class PayStoreBankCardServiceImpl implements PayStoreBankCardService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ApiPayStoreBindBankCardController.class);
	
	@Resource
	private StoreBankCardMapper storeBankCardMapper;
	
	@Autowired
	private Cache cache;

	@Override
	public StoreBankCardVO findStoreBankCardInfo(StoreBankCardCondition condition) {
		StoreBankCardVO storeBankCard = new StoreBankCardVO();
		storeBankCard = storeBankCardMapper.selectStorBankCardInfo(condition);
		return storeBankCard;
	}
	
	@Override
	public int saveStoreBankCard(StoreBankCard condition) {
		// 校验用户填入的信息是否完善
    	String bankName = condition.getBankName();
    	if(StringUtils.isEmpty(bankName)){
    		LOGGER.info("业务异常："+BusinessCode.CODE_610011);
    		throw new BusinessException(BusinessCode.CODE_610011);
    	}
    	String cardNumber = condition.getCardNumber();
    	if(StringUtils.isEmpty(cardNumber)){
    		LOGGER.info("业务异常："+BusinessCode.CODE_610012);
    		throw new BusinessException(BusinessCode.CODE_610012);
    	}
    	String bankUserName = condition.getBankUserName();
    	if(StringUtils.isEmpty(bankUserName)){
    		LOGGER.info("业务异常："+BusinessCode.CODE_610013);
    		throw new BusinessException(BusinessCode.CODE_610013);
    	}
    	String bandBranchName = condition.getBandBranchName();
    	if(StringUtils.isEmpty(bandBranchName)){
    		LOGGER.info("业务异常："+BusinessCode.CODE_610014);
    		throw new BusinessException(BusinessCode.CODE_610014);
    	}
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
    	String code = cache.get(CacheName.PAY_VERIFICATION_CODE+ currentStoreUser.getBusinessId());
    	if(!code.equals(verificationCode)){
    		LOGGER.info("业务异常："+BusinessCode.CODE_610019);
    		throw new BusinessException(BusinessCode.CODE_610019);
    	}
    	condition.setCreated(new Date());
    	condition.setUpdated(new Date());
    	condition.setCreatedBy(currentStoreUser.getBusinessId());
    	condition.setUpdatedBy(currentStoreUser.getBusinessId());
    	condition.setCreatedByName(condition.getBankUserName());
    	condition.setUpdatedByName(condition.getBankUserName());
		int res = storeBankCardMapper.insertStoreBankCardinfo(condition);
		return res;
	}

}
