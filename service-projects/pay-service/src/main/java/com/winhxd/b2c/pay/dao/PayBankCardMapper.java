package com.winhxd.b2c.pay.dao;

import java.util.List;
import java.util.Map;

import com.winhxd.b2c.common.domain.pay.condition.StoreBankCardCondition;
import com.winhxd.b2c.common.domain.pay.model.PayBankCard;
import com.winhxd.b2c.common.domain.pay.vo.StoreBankCardVO;

public interface PayBankCardMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PayBankCard record);

    int insertSelective(PayBankCard record);

	PayBankCard selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PayBankCard record);

    int updateByPrimaryKey(PayBankCard record);

	StoreBankCardVO selectStorBankCardInfo(StoreBankCardCondition condition);

	int insertStoreBankCardinfo(PayBankCard condition);
	
	List<PayBankCard> selectByStoreId(Long storeId);
	
	/**
	 * @author liuhanning
	 * @date  2018年8月27日 上午1:22:15
	 * @Description 根据银行卡号、代码、门店id获取绑定信息
	 * @param map
	 * @return
	 */
	List<PayBankCard> selectByStoreIdAndCardNumber(Map<String, Object> map);
}