package com.winhxd.b2c.promotion.dao;

import com.winhxd.b2c.common.domain.promotion.model.CouponGrade;
import com.winhxd.b2c.common.domain.promotion.vo.CouponGradeVO;
import org.apache.ibatis.annotations.Param;

public interface CouponGradeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CouponGrade record);

    int insertSelective(CouponGrade record);

    CouponGrade selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CouponGrade record);

    int updateByPrimaryKey(CouponGrade record);

    CouponGradeVO viewCouponGradeDetail(@Param("id") long id);

    int updateCouponGradeValid(@Param("id")long id,@Param("userId") long userId,@Param("userName") String userName);


}