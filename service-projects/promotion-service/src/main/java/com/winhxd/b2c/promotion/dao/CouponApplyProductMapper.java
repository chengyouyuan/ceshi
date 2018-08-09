package com.winhxd.b2c.promotion.dao;

import com.winhxd.b2c.common.domain.promotion.model.CouponApplyProduct;

public interface CouponApplyProductMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CouponApplyProduct record);

    int insertSelective(CouponApplyProduct record);

    CouponApplyProduct selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CouponApplyProduct record);

    int updateByPrimaryKey(CouponApplyProduct record);

    long insertCouponApplyProduct(CouponApplyProduct couponApplyProduct);
}