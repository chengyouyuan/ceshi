package com.winhxd.b2c.pay.dao;

import com.winhxd.b2c.common.domain.pay.model.PayStoreTransactionRecord;

public interface PayStoreTransactionRecordMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PayStoreTransactionRecord record);

    int insertSelective(PayStoreTransactionRecord record);

    PayStoreTransactionRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PayStoreTransactionRecord record);

    int updateByPrimaryKey(PayStoreTransactionRecord record);
}