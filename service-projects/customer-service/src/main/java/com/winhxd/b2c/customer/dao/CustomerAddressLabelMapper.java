package com.winhxd.b2c.customer.dao;

import com.winhxd.b2c.common.domain.customer.model.CustomerAddressLabel;
import com.winhxd.b2c.common.domain.customer.vo.CustomerAddressLabelVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomerAddressLabelMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CustomerAddressLabel record);

    int insertSelective(CustomerAddressLabel record);

    CustomerAddressLabel selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CustomerAddressLabel record);

    int updateByPrimaryKey(CustomerAddressLabel record);

    List<CustomerAddressLabelVO> selectCustomerAddressLabelByUserId(@Param("customerId") Long customerId);
}