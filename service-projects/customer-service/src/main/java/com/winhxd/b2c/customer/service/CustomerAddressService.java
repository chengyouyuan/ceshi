package com.winhxd.b2c.customer.service;

import com.winhxd.b2c.common.context.CustomerUser;
import com.winhxd.b2c.common.domain.customer.condition.CustomerAddressCondition;
import com.winhxd.b2c.common.domain.customer.condition.CustomerAddressSelectCondition;
import com.winhxd.b2c.common.domain.customer.vo.CustomerAddressVO;

import java.util.List;


public interface CustomerAddressService {
    int deleteByPrimaryKey(CustomerAddressSelectCondition condition);

    int insert(CustomerAddressCondition customerAddressCondition,CustomerUser customerUser);

    CustomerAddressVO selectByPrimaryKey(CustomerAddressSelectCondition condition);

    int updateByPrimaryKey(CustomerAddressCondition condition);

    List<CustomerAddressVO> getCustomerAddressByUserId(Long userId);
}
