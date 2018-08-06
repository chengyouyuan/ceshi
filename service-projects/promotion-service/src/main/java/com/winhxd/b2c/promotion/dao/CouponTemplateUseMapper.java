package com.winhxd.b2c.promotion.dao;

import com.winhxd.b2c.common.domain.promotion.model.CouponTemplateUse;

public interface CouponTemplateUseMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CouponTemplateUse record);

    int insertSelective(CouponTemplateUse record);

    CouponTemplateUse selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CouponTemplateUse record);

    int updateByPrimaryKey(CouponTemplateUse record);
}