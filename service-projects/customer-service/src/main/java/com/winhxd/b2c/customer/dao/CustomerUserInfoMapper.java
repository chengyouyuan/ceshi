package com.winhxd.b2c.customer.dao;

import com.winhxd.b2c.common.domain.system.login.model.CustomerUserInfo;

public interface CustomerUserInfoMapper {
    int deleteByPrimaryKey(Long customerId);

    int insert(CustomerUserInfo record);

    int insertSelective(CustomerUserInfo record);

    CustomerUserInfo selectByPrimaryKey(Long customerId);

    int updateByPrimaryKeySelective(CustomerUserInfo record);

    int updateByPrimaryKey(CustomerUserInfo record);
}