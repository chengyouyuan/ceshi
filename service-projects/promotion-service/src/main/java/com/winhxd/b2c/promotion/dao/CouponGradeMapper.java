package com.winhxd.b2c.promotion.dao;

import com.winhxd.b2c.common.domain.promotion.model.CouponGrade;

public interface CouponGradeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CouponGrade record);

    int insertSelective(CouponGrade record);

    CouponGrade selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CouponGrade record);

    int updateByPrimaryKey(CouponGrade record);
}