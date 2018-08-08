package com.winhxd.b2c.promotion.dao;

import com.winhxd.b2c.common.domain.promotion.model.CouponInvestorDetail;

public interface CouponInvestorDetailMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CouponInvestorDetail record);

    int insertSelective(CouponInvestorDetail record);

    CouponInvestorDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CouponInvestorDetail record);

    int updateByPrimaryKey(CouponInvestorDetail record);
}