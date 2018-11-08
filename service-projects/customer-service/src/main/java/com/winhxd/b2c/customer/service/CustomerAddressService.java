package com.winhxd.b2c.customer.service;

import com.winhxd.b2c.common.context.CustomerUser;
import com.winhxd.b2c.common.domain.customer.condition.CustomerAddressCondition;
import com.winhxd.b2c.common.domain.customer.model.CustomerAddress;


public interface CustomerAddressService {
    int deleteByPrimaryKey(Long id);

    int insert(CustomerAddressCondition customerAddressCondition,CustomerUser customerUser);

    CustomerAddress selectByPrimaryKey(Long id);

    int updateByPrimaryKey(CustomerAddressCondition customerAddressCondition,CustomerUser customerUser);
}
