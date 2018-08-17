package com.winhxd.b2c.pay.dao;

import java.util.List;

import com.winhxd.b2c.common.domain.pay.model.PayStoreWallet;

public interface PayStoreWalletMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PayStoreWallet record);

    int insertSelective(PayStoreWallet record);

    PayStoreWallet selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PayStoreWallet record);

    int updateByPrimaryKey(PayStoreWallet record);
    
    List<PayStoreWallet> selectByStoreId(Long storeId);
}