package com.winhxd.b2c.promotion.dao;

import com.winhxd.b2c.common.domain.promotion.model.CouponActivityTemplate;

public interface CouponActivityTemplateMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CouponActivityTemplate record);

    int insertSelective(CouponActivityTemplate record);

    CouponActivityTemplate selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CouponActivityTemplate record);

    int updateByPrimaryKey(CouponActivityTemplate record);
}