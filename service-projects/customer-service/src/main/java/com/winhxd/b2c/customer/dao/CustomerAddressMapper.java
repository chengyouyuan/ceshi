package com.winhxd.b2c.customer.dao;

import com.winhxd.b2c.common.domain.customer.model.CustomerAddress;

public interface CustomerAddressMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CustomerAddress record);

    int insertSelective(CustomerAddress record);

    CustomerAddress selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CustomerAddress record);

    int updateByPrimaryKey(CustomerAddress record);
}