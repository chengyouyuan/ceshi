package com.winhxd.b2c.customer.dao;

import com.winhxd.b2c.common.domain.customer.condition.CustomerAddressQueryCondition;
import com.winhxd.b2c.common.domain.customer.model.CustomerAddress;
import com.winhxd.b2c.common.domain.customer.vo.CustomerAddressVO;

import java.util.List;

public interface CustomerAddressMapper {

    int deleteByPrimaryKey(Long id);

    int insertSelective(CustomerAddress record);

    CustomerAddressVO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CustomerAddress customerAddress);

    int updateByPrimaryKeySelectiveSupportLableNull(CustomerAddress customerAddress);

    List<CustomerAddressVO> selectCustomerAddressByUserId(Long userId);

    int setDefaultCustomerAddressFalse(Long customerId);

    int updateCustomerAddressByLabel(CustomerAddress condition);

    List<CustomerAddressVO> selectCustomerAddressByCondtion(CustomerAddressQueryCondition condition);

}