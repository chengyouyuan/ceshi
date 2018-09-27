package com.winhxd.b2c.promotion.dao;

import com.winhxd.b2c.common.domain.promotion.model.CouponPushCustomer;

public interface CouponPushCustomerMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CouponPushCustomer record);

    int insertSelective(CouponPushCustomer record);

    CouponPushCustomer selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CouponPushCustomer record);

    int updateByPrimaryKey(CouponPushCustomer record);
}