package com.winhxd.b2c.promotion.dao;

import com.winhxd.b2c.common.domain.promotion.model.CouponActivityArea;

public interface CouponActivityAreaMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CouponActivityArea record);

    int insertSelective(CouponActivityArea record);

    CouponActivityArea selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CouponActivityArea record);

    int updateByPrimaryKey(CouponActivityArea record);
}