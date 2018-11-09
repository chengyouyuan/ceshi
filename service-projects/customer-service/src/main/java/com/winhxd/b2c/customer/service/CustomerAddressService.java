package com.winhxd.b2c.customer.service;

import com.winhxd.b2c.common.context.CustomerUser;
import com.winhxd.b2c.common.domain.customer.condition.CustomerAddressCondition;
import com.winhxd.b2c.common.domain.customer.condition.CustomerAddressLabelCondition;
import com.winhxd.b2c.common.domain.customer.condition.CustomerAddressSelectCondition;
import com.winhxd.b2c.common.domain.customer.vo.CustomerAddressVO;

import java.util.List;


public interface CustomerAddressService {
    int deleteByPrimaryKey(CustomerAddressSelectCondition condition);

    int insert(CustomerAddressCondition customerAddressCondition,CustomerUser customerUser);

    CustomerAddressVO selectByPrimaryKey(CustomerAddressSelectCondition condition);

    int updateByPrimaryKey(CustomerAddressCondition condition);

    List<CustomerAddressVO> getCustomerAddressByUserId(Long userId);

    /**
     * 根据用户id查询用户地址标签
     */
    List<String> findCustomerAddressLabelByUserId(Long customerId);

    /**
     * 新增用户地址标签
     */
    int saveCustomerAddressLabel(CustomerAddressLabelCondition customerAddressLabelCondition);

    /**
     * 删除用户地址标签
     */
    int deleteCustomerAddressLabel(CustomerAddressLabelCondition customerAddressLabelCondition);
}
