package com.winhxd.b2c.order.dao;

import com.winhxd.b2c.common.domain.promotion.model.CouponStayReceiveDetail;

public interface CouponStayReceiveDetailMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CouponStayReceiveDetail record);

    int insertSelective(CouponStayReceiveDetail record);

    CouponStayReceiveDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CouponStayReceiveDetail record);

    int updateByPrimaryKey(CouponStayReceiveDetail record);
}