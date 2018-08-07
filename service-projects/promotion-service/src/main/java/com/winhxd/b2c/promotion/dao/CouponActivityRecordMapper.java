package com.winhxd.b2c.promotion.dao;

import com.winhxd.b2c.common.domain.promotion.model.CouponActivityRecord;

import java.util.List;

public interface CouponActivityRecordMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CouponActivityRecord record);

    int insertSelective(CouponActivityRecord record);

    CouponActivityRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CouponActivityRecord record);

    int updateByPrimaryKey(CouponActivityRecord record);

    /**
     * 根据条件查询
     * @param example
     * @return
     */
    List<CouponActivityRecord> selectByExample(CouponActivityRecord example);
}