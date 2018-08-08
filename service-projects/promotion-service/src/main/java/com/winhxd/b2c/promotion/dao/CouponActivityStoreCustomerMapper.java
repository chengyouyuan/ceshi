package com.winhxd.b2c.promotion.dao;

import com.winhxd.b2c.common.domain.promotion.model.CouponActivityStoreCustomer;

import java.util.List;

public interface CouponActivityStoreCustomerMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CouponActivityStoreCustomer record);

    int insertSelective(CouponActivityStoreCustomer record);

    CouponActivityStoreCustomer selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CouponActivityStoreCustomer record);

    int updateByPrimaryKey(CouponActivityStoreCustomer record);

    List<CouponActivityStoreCustomer> selectByTemplateId(Long couponActivityTemplateId);
}