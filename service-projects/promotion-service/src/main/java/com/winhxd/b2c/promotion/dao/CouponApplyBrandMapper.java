package com.winhxd.b2c.promotion.dao;

import com.winhxd.b2c.common.domain.promotion.model.CouponApplyBrand;

public interface CouponApplyBrandMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CouponApplyBrand record);

    int insertSelective(CouponApplyBrand record);

    CouponApplyBrand selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CouponApplyBrand record);

    int updateByPrimaryKey(CouponApplyBrand record);
}