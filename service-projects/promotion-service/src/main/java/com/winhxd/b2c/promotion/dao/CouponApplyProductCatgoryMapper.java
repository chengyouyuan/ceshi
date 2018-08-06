package com.winhxd.b2c.promotion.dao;

import com.winhxd.b2c.common.domain.promotion.model.CouponApplyProductCatgory;

public interface CouponApplyProductCatgoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CouponApplyProductCatgory record);

    int insertSelective(CouponApplyProductCatgory record);

    CouponApplyProductCatgory selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CouponApplyProductCatgory record);

    int updateByPrimaryKey(CouponApplyProductCatgory record);
}