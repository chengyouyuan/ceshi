package com.winhxd.b2c.promotion.dao;

import com.winhxd.b2c.common.domain.promotion.model.CouponApplyBrandList;
import com.winhxd.b2c.common.domain.promotion.model.CouponApplyProductList;

import java.util.List;

public interface CouponApplyProductListMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CouponApplyProductList record);

    int insertSelective(CouponApplyProductList record);

    CouponApplyProductList selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CouponApplyProductList record);

    int updateByPrimaryKey(CouponApplyProductList record);

    List<CouponApplyProductList> selectByApplyProductId(Long id);

    List<CouponApplyBrandList> selectByApplyBrandId(Long id);
}