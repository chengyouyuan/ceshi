package com.winhxd.b2c.promotion.dao;

import com.winhxd.b2c.common.domain.promotion.model.CouponInvestor;

public interface CouponInvestorMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CouponInvestor record);

    int insertSelective(CouponInvestor record);

    CouponInvestor selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CouponInvestor record);

    int updateByPrimaryKey(CouponInvestor record);
}