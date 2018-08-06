package com.winhxd.b2c.promotion.dao;

import com.winhxd.b2c.common.domain.promotion.model.CouponActivityDetail;

public interface CouponActivityDetailMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CouponActivityDetail record);

    int insertSelective(CouponActivityDetail record);

    CouponActivityDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CouponActivityDetail record);

    int updateByPrimaryKey(CouponActivityDetail record);
}