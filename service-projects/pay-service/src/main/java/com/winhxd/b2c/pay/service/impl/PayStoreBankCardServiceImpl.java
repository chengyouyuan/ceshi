package com.winhxd.b2c.pay.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.winhxd.b2c.common.cache.Cache;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.CacheName;
import com.winhxd.b2c.common.context.StoreUser;
import com.winhxd.b2c.common.context.UserContext;
import com.winhxd.b2c.common.domain.pay.condition.StoreBankCardCondition;
import com.winhxd.b2c.common.domain.pay.enums.StatusEnums;
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
	
	@Resource
	private Cache redisClusterCache;

	@Override
	public StoreBankCardVO findStoreBankCardInfo(StoreBankCardCondition condition) {
		StoreBankCardVO storeBankCard = new StoreBankCardVO();
		storeBankCard = storeBankCardMapper.selectStorBankCardInfo(condition);
		return storeBankCard;
	}
	
	@Override
	public int saveStoreBankCard(StoreBankCardCondition condition) {
		int res = 0;
		// 校验用户填入的信息是否完善
    	String bankName = condition.getBankName();
    	if(StringUtils.isEmpty(bankName)){
    		LOGGER.info("业务异常："+BusinessCode.CODE_610011);
    		res = BusinessCode.CODE_610011;
    		throw new BusinessException(BusinessCode.CODE_610011);
    	}
    	String cardNumber = condition.getCardNumber();
    	if(StringUtils.isEmpty(cardNumber)){
    		LOGGER.info("业务异常："+BusinessCode.CODE_610012);
    		res = BusinessCode.CODE_610012;
    		throw new BusinessException(BusinessCode.CODE_610012);
    	}
    	String bankUserName = condition.getBankUserName();
    	if(StringUtils.isEmpty(bankUserName)){
    		LOGGER.info("业务异常："+BusinessCode.CODE_610013);
    		res = BusinessCode.CODE_610013;
    		throw new BusinessException(BusinessCode.CODE_610013);
    	}
    	String bandBranchName = condition.getBandBranchName();
    	if(StringUtils.isEmpty(bandBranchName)){
    		LOGGER.info("业务异常："+BusinessCode.CODE_610014);
    		res = BusinessCode.CODE_610014;
    		throw new BusinessException(BusinessCode.CODE_610014);
    	}
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
    	String swiftcode = condition.getSwiftCode();
    	if(StringUtils.isEmpty(swiftcode)){
    		LOGGER.info("业务异常："+BusinessCode.CODE_610029);
    		res = BusinessCode.CODE_610029;
    		throw new BusinessException(BusinessCode.CODE_610029);
    	}
    	StoreUser currentStoreUser = UserContext.getCurrentStoreUser();
    ///////////////////测试假数据///////////////////////
//    	StoreUser currentStoreUser = new StoreUser();
//    	currentStoreUser.setBusinessId(130l);
   ////////////////////////////////////////////////////
    
    	if(currentStoreUser != null){
    		Boolean exists = redisClusterCache.exists(CacheName.PAY_VERIFICATION_CODE+2+"_"+currentStoreUser.getBusinessId());
    		System.out.print("验证码是否存在-----------"+exists);
    		if(!exists){
    			LOGGER.info("业务异常："+BusinessCode.CODE_610020);
    			res = BusinessCode.CODE_610020;
    			throw new BusinessException(BusinessCode.CODE_610020);
    		}
    		String code = redisClusterCache.get(CacheName.PAY_VERIFICATION_CODE+2+"_"+currentStoreUser.getBusinessId());
			if(!verificationCode.equals(code)){
				LOGGER.info("业务异常："+BusinessCode.CODE_610019);
				res = BusinessCode.CODE_610019;
				throw new BusinessException(BusinessCode.CODE_610019);
			}
    		
    		// 判断当前门店是否绑定过当前要绑定的银行卡信息
    		LOGGER.info("当前门店即将要绑定的银行卡信息----："+ condition);
    		StoreBankCardVO storBankCardInfo = storeBankCardMapper.selectStorBankCardInfo(condition);
    		
        	StoreBankCard storeBankCard = new StoreBankCard();
        	BeanUtils.copyProperties(condition, storeBankCard);
    		
        	storeBankCard.setStoreId(currentStoreUser.getBusinessId());
        	storeBankCard.setStatus(StatusEnums.EFFECTIVE.getCode()); 
        	storeBankCard.setCreated(new Date());
        	storeBankCard.setUpdated(new Date());
        	storeBankCard.setCreatedBy(currentStoreUser.getBusinessId());
        	storeBankCard.setUpdatedBy(currentStoreUser.getBusinessId());
        	storeBankCard.setCreatedByName(condition.getBankUserName());
        	storeBankCard.setUpdatedByName(condition.getBankUserName());
    		if(storBankCardInfo != null && storBankCardInfo.getStoreId().equals(currentStoreUser.getBusinessId())){// 判断当前是否是同一个门店
				Long id = storBankCardInfo.getId();
				storeBankCard.setId(id);
				storeBankCardMapper.updateByPrimaryKeySelective(storeBankCard);
    		}else{
    			storeBankCardMapper.insertStoreBankCardinfo(storeBankCard);
    		}
    	}
		return res;
	}

}
