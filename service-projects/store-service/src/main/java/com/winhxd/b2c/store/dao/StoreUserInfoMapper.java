package com.winhxd.b2c.store.dao;

import com.winhxd.b2c.common.domain.system.login.model.StoreUserInfo;

public interface StoreUserInfoMapper {
    int deleteByPrimaryKey(Long businessId);

    int insert(StoreUserInfo record);

    int insertSelective(StoreUserInfo record);

    StoreUserInfo selectByPrimaryKey(Long businessId);

    int updateByPrimaryKeySelective(StoreUserInfo record);

    int updateByPrimaryKey(StoreUserInfo record);
}