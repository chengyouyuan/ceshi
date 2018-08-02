package com.winhxd.b2c.store.dao;

import com.winhxd.b2c.common.domain.store.model.StoreProductManage;

public interface StoreProductManageMapper {
    int deleteByPrimaryKey(Long id);

    int insert(StoreProductManage record);

    int insertSelective(StoreProductManage record);

    StoreProductManage selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StoreProductManage record);

    int updateByPrimaryKey(StoreProductManage record);
}