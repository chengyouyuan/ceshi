package com.winhxd.b2c.promotion.dao;

import com.winhxd.b2c.common.domain.promotion.model.CouponGradeDetail;

import java.util.List;

public interface CouponGradeDetailMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CouponGradeDetail record);

    int insertSelective(CouponGradeDetail record);

    CouponGradeDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CouponGradeDetail record);

    int updateByPrimaryKey(CouponGradeDetail record);

    /**
     *
     * @param gradeId
     * @return
     */
    List<CouponGradeDetail> selectByGradeId(Long gradeId);
}