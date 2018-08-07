package com.winhxd.b2c.promotion.dao;

import com.winhxd.b2c.common.domain.promotion.model.CouponDict;

public interface CouponDictMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CouponDict record);

    int insertSelective(CouponDict record);

    CouponDict selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CouponDict record);

    int updateByPrimaryKey(CouponDict record);
}