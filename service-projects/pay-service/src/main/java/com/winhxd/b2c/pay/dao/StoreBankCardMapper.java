package com.winhxd.b2c.pay.dao;

import com.winhxd.b2c.common.domain.pay.condition.StoreBankCardCondition;
import com.winhxd.b2c.common.domain.pay.model.StoreBankCard;
import com.winhxd.b2c.common.domain.pay.vo.StoreBankCardVO;

public interface StoreBankCardMapper {
    int deleteByPrimaryKey(Long id);

    int insert(StoreBankCard record);

    int insertSelective(StoreBankCard record);

    StoreBankCard selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StoreBankCard record);

    int updateByPrimaryKey(StoreBankCard record);

	StoreBankCardVO selectStorBankCardInfo(StoreBankCardCondition condition);

	int insertStoreBankCardinfo(StoreBankCard condition);
}