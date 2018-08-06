package com.winhxd.b2c.promotion.dao;

import com.winhxd.b2c.common.domain.promotion.model.CouponActivityRecord;

public interface CouponActivityRecordMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CouponActivityRecord record);

    int insertSelective(CouponActivityRecord record);

    CouponActivityRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CouponActivityRecord record);

    int updateByPrimaryKey(CouponActivityRecord record);
}