package com.winhxd.b2c.promotion.dao;

import com.winhxd.b2c.common.domain.promotion.model.CouponApplyBrandList;

public interface CouponApplyBrandListMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CouponApplyBrandList record);

    int insertSelective(CouponApplyBrandList record);

    CouponApplyBrandList selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CouponApplyBrandList record);

    int updateByPrimaryKey(CouponApplyBrandList record);
}