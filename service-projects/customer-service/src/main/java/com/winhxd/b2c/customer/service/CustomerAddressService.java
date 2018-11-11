package com.winhxd.b2c.customer.service;

import com.winhxd.b2c.common.context.CustomerUser;
import com.winhxd.b2c.common.domain.customer.condition.CustomerAddressCondition;
import com.winhxd.b2c.common.domain.customer.condition.CustomerAddressLabelCondition;
import com.winhxd.b2c.common.domain.customer.condition.CustomerAddressSelectCondition;
import com.winhxd.b2c.common.domain.customer.vo.CustomerAddressLabelVO;
import com.winhxd.b2c.common.domain.customer.vo.CustomerAddressVO;


import java.util.List;


public interface CustomerAddressService {

    int deleteCustomerAddress(CustomerAddressSelectCondition condition);

    int saveCustomerAddress(CustomerAddressCondition customerAddressCondition,CustomerUser customerUser);

    CustomerAddressVO selectCustomerAddressById(Long customerAddressId);

    /**
     * 查询用户默认收货地址
     * @param customerUser
     * @return
     */
    CustomerAddressVO selectDefaultCustomerAddress(CustomerUser customerUser);

    int updateCustomerAddress(CustomerAddressCondition condition,CustomerUser customerUser);

    int updateDefaultCustomerAddress(Long customerAddressId,CustomerUser customerUser);

    List<CustomerAddressVO> selectCustomerAddressByUserId(Long userId);

    /**
     * 根据用户id查询用户地址标签
     */
    List<CustomerAddressLabelVO> selectAddressLabelByUserId(Long customerId);

    /**
     * 新增用户地址标签
     */
    int saveCustomerAddressLabel(CustomerAddressLabelCondition customerAddressLabelCondition);

    /**
     * 删除用户地址标签
     */
    int deleteCustomerAddressLabel(CustomerAddressLabelCondition customerAddressLabelCondition);
}
