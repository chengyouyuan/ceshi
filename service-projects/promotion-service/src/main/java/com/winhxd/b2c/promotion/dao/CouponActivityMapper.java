package com.winhxd.b2c.promotion.dao;

import com.winhxd.b2c.common.domain.promotion.model.CouponActivity;

import java.util.List;

public interface CouponActivityMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CouponActivity record);

    int insertSelective(CouponActivity record);

    CouponActivity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CouponActivity record);

    int updateByPrimaryKey(CouponActivity record);

    /**
     * 根据条件查询
     * @param example
     * @return
     */
    List<CouponActivity> selectByExample(CouponActivity example);
}