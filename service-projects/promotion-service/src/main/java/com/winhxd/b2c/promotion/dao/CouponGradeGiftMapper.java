package com.winhxd.b2c.promotion.dao;

import com.winhxd.b2c.common.domain.promotion.model.CouponGradeGift;

public interface CouponGradeGiftMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CouponGradeGift record);

    int insertSelective(CouponGradeGift record);

    CouponGradeGift selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CouponGradeGift record);

    int updateByPrimaryKey(CouponGradeGift record);
}