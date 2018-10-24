package com.winhxd.b2c.pay.service.impl;

import com.winhxd.b2c.common.cache.Cache;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.CacheName;
import com.winhxd.b2c.common.context.StoreUser;
import com.winhxd.b2c.common.context.UserContext;
import com.winhxd.b2c.common.domain.pay.condition.StoreBankCardCondition;
import com.winhxd.b2c.common.domain.pay.enums.StatusEnums;
import com.winhxd.b2c.common.domain.pay.model.PayBankCard;
import com.winhxd.b2c.common.domain.pay.vo.StoreBankCardVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.util.JsonUtil;
import com.winhxd.b2c.pay.api.ApiPayStoreBindBankCardController;
import com.winhxd.b2c.pay.dao.PayBankCardMapper;
import com.winhxd.b2c.pay.service.PayStoreBankCardService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PayStoreBankCardServiceImpl implements PayStoreBankCardService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ApiPayStoreBindBankCardController.class);
	
	@Resource
	private PayBankCardMapper payBankCardMapper;
	
	@Resource
	private Cache redisClusterCache;

	@Override
	public StoreBankCardVO findStoreBankCardInfo(StoreBankCardCondition condition) {
		StoreBankCardVO storeBankCard = new StoreBankCardVO();
		storeBankCard = payBankCardMapper.selectStorBankCardInfo(condition);
		return storeBankCard;
	}
	
	@Override
	public int saveStoreBankCard(StoreBankCardCondition condition) {
		int res = 0;
    	StoreUser currentStoreUser = UserContext.getCurrentStoreUser();
    ///////////////////测试假数据///////////////////////
//    	StoreUser currentStoreUser = new StoreUser();
//    	currentStoreUser.setBusinessId(84l);
   ////////////////////////////////////////////////////
    		Boolean exists = redisClusterCache.exists(CacheName.PAY_VERIFICATION_CODE+2+"_"+currentStoreUser.getBusinessId());
    		if(!exists){
                LOGGER.info("业务异常：验证码已失效");
    			throw new BusinessException(BusinessCode.CODE_610020);
    		}
    		String code = redisClusterCache.get(CacheName.PAY_VERIFICATION_CODE+2+"_"+currentStoreUser.getBusinessId());
		if (!condition.getVerificationCode().equals(code)) {
            LOGGER.info("业务异常：验证码输入不正确");
				throw new BusinessException(BusinessCode.CODE_610019);
			}
    		
    		// 判断当前门店是否绑定过当前要绑定的银行卡信息
        LOGGER.info("当前门店即将要绑定的银行卡信息----[{}]", JsonUtil.toJSONString(condition));
    		//绑定新的银行卡
    		PayBankCard payBankCard = new PayBankCard();
        	BeanUtils.copyProperties(condition, payBankCard);

			payBankCard.setStoreId(currentStoreUser.getBusinessId());
			payBankCard.setStatus(StatusEnums.EFFECTIVE.getCode());
			payBankCard.setCreated(new Date());
			payBankCard.setUpdated(new Date());
			payBankCard.setCreatedBy(currentStoreUser.getBusinessId());
			payBankCard.setUpdatedBy(currentStoreUser.getBusinessId());
			payBankCard.setCreatedByName(condition.getBankUserName());
			payBankCard.setUpdatedByName(condition.getBankUserName());
        	
        	Map<String, Object> map=new HashMap<>();
			map.put("storeId", currentStoreUser.getBusinessId());
			map.put("cardNumber", condition.getCardNumber());
			map.put("swiftCode", condition.getSwiftCode());
			List<PayBankCard> bankCards = payBankCardMapper.selectByStoreIdAndCardNumber(map);
    		if(CollectionUtils.isNotEmpty(bankCards)){
				Long id = bankCards.get(0).getId();
				payBankCard.setId(id);
				payBankCard.setUpdated(new Date());
				payBankCardMapper.updateByPrimaryKeySelective(payBankCard);
    		}else{
    			payBankCardMapper.insertStoreBankCardinfo(payBankCard);
    		}
		return res;
	}

}
