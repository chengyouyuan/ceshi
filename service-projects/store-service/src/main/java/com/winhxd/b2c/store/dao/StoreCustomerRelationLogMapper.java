package com.winhxd.b2c.store.dao;

import com.winhxd.b2c.common.domain.store.model.StoreCustomerRelationLog;

import java.util.List;

public interface StoreCustomerRelationLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(StoreCustomerRelationLog record);

    int insertSelective(StoreCustomerRelationLog record);

    StoreCustomerRelationLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StoreCustomerRelationLog record);

    int updateByPrimaryKey(StoreCustomerRelationLog record);

    int batchAddStoreCustomerRelationLog(List<StoreCustomerRelationLog> storeCustomerRelationLogs);
}