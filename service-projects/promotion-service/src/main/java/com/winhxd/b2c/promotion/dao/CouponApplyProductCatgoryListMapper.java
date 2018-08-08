package com.winhxd.b2c.promotion.dao;

import com.winhxd.b2c.common.domain.promotion.model.CouponApplyProductCatgoryList;

public interface CouponApplyProductCatgoryListMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CouponApplyProductCatgoryList record);

    int insertSelective(CouponApplyProductCatgoryList record);

    CouponApplyProductCatgoryList selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CouponApplyProductCatgoryList record);

    int updateByPrimaryKey(CouponApplyProductCatgoryList record);
}