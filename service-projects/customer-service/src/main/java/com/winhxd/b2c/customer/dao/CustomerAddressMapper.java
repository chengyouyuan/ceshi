package com.winhxd.b2c.customer.dao;

import com.winhxd.b2c.common.domain.customer.model.CustomerAddress;
import com.winhxd.b2c.common.domain.customer.vo.CustomerAddressVO;

import java.util.List;

public interface CustomerAddressMapper {
    int deleteByPrimaryKey(Long id);


    int insertSelective(CustomerAddress record);

    CustomerAddressVO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CustomerAddressVO record);


    List<CustomerAddressVO> selectCustomerAddressByUserId(Long userId);
}