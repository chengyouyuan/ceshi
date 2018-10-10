package com.winhxd.b2c.pay.dao;

import com.winhxd.b2c.common.domain.pay.model.PayBankroll;
import org.apache.ibatis.annotations.Param;

public interface PayBankrollMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PayBankroll record);

    int insertSelective(PayBankroll record);

    PayBankroll selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PayBankroll record);

    int updateByPrimaryKey(PayBankroll record);

    PayBankroll selectStoreBankrollByStoreId(@Param("storeId") Long storeId);
}