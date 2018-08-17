package com.winhxd.b2c.promotion.dao;

import com.winhxd.b2c.common.domain.promotion.model.CouponActivityArea;

import java.util.List;

public interface CouponActivityAreaMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CouponActivityArea record);

    int insertSelective(CouponActivityArea record);

    CouponActivityArea selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CouponActivityArea record);

    int updateByPrimaryKey(CouponActivityArea record);

    /**
     * 根据活动id查询区域信息
     * @param couponActivityId
     * @return
     */
    List<CouponActivityArea> selectAreaByActivityId(Long couponActivityId);
}