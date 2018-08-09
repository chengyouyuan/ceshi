package com.winhxd.b2c.promotion.dao;

import com.winhxd.b2c.common.domain.promotion.model.CouponApply;
import com.winhxd.b2c.common.domain.promotion.vo.CouponApplyVO;
import org.apache.ibatis.annotations.Param;

public interface CouponApplyMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CouponApply record);

    int insertSelective(CouponApply record);

    CouponApply selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CouponApply record);

    int updateByPrimaryKey(CouponApply record);

    CouponApplyVO viewCouponApplyDetail(@Param("id") long id);

    int updateCouponGradeValid(long id, long userId, String userName);
}