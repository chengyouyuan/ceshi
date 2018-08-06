package com.winhxd.b2c.promotion.dao;

import com.winhxd.b2c.common.domain.promotion.model.CouponStayReceive;

public interface CouponStayReceiveMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CouponStayReceive record);

    int insertSelective(CouponStayReceive record);

    CouponStayReceive selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CouponStayReceive record);

    int updateByPrimaryKey(CouponStayReceive record);
}