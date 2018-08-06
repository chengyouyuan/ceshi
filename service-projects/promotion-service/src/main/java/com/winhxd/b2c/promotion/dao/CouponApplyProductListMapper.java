package com.winhxd.b2c.promotion.dao;

import com.winhxd.b2c.common.domain.promotion.model.CouponApplyProductList;

public interface CouponApplyProductListMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CouponApplyProductList record);

    int insertSelective(CouponApplyProductList record);

    CouponApplyProductList selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CouponApplyProductList record);

    int updateByPrimaryKey(CouponApplyProductList record);
}