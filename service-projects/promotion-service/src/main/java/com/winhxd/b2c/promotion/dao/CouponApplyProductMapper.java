package com.winhxd.b2c.promotion.dao;

import com.winhxd.b2c.common.domain.promotion.model.CouponApplyProduct;

public interface CouponApplyProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CouponApplyProduct record);

    int insertSelective(CouponApplyProduct record);

    CouponApplyProduct selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CouponApplyProduct record);

    int updateByPrimaryKey(CouponApplyProduct record);
}