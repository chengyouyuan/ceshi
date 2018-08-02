package com.winhxd.b2c.store.dao;

import com.winhxd.b2c.common.domain.store.model.StoreSubmitProduct;

public interface StoreSubmitProductMapper {
    int deleteByPrimaryKey(Long id);

    int insert(StoreSubmitProduct record);

    int insertSelective(StoreSubmitProduct record);

    StoreSubmitProduct selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StoreSubmitProduct record);

    int updateByPrimaryKey(StoreSubmitProduct record);
}