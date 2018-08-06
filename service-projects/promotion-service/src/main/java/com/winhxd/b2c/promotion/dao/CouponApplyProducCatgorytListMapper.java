package com.winhxd.b2c.promotion.dao;

import com.winhxd.b2c.common.domain.promotion.model.CouponApplyProducCatgorytList;

public interface CouponApplyProducCatgorytListMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CouponApplyProducCatgorytList record);

    int insertSelective(CouponApplyProducCatgorytList record);

    CouponApplyProducCatgorytList selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CouponApplyProducCatgorytList record);

    int updateByPrimaryKey(CouponApplyProducCatgorytList record);
}