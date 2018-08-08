package com.winhxd.b2c.promotion.dao;

import com.winhxd.b2c.common.domain.promotion.model.CouponInvestor;

public interface CouponInvestorMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CouponInvestor record);

    int insertSelective(CouponInvestor record);

    CouponInvestor selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CouponInvestor record);

    int updateByPrimaryKey(CouponInvestor record);

    Long insertCouponInvestor(CouponInvestor couponInvestor);
}