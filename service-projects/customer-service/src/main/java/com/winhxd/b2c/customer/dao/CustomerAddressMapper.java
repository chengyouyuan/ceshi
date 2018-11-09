package com.winhxd.b2c.customer.dao;

import com.winhxd.b2c.common.domain.customer.condition.CustomerAddressQueryCondition;
import com.winhxd.b2c.common.domain.customer.model.CustomerAddress;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomerAddressMapper {
    int deleteByPrimaryKey(Long id);


    int insertSelective(CustomerAddress record);

    CustomerAddress selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CustomerAddress record);


    List<CustomerAddress> selectCustomerAddressByUserId(Long userId);

    List<CustomerAddress> selectCustomerAddressByLabelId(@Param("labelId") Long labelId, @Param("customerId") Long customerId);

    int updateCustomerAddressById(List<Long> list);

    List<CustomerAddress> selectCustomerAddressByCondtion(CustomerAddressQueryCondition condition);

}