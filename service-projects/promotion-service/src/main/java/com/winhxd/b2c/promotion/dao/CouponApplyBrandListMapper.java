package com.winhxd.b2c.promotion.dao;

import com.winhxd.b2c.common.domain.promotion.model.CouponApplyBrandList;

import java.util.List;

public interface CouponApplyBrandListMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CouponApplyBrandList record);

    int insertSelective(CouponApplyBrandList record);

    CouponApplyBrandList selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CouponApplyBrandList record);

    int updateByPrimaryKey(CouponApplyBrandList record);

    /**
     *
     * @param applyBrandId
     * @return
     */
    List<CouponApplyBrandList> selectByApplyBrandId(Long applyBrandId);
}