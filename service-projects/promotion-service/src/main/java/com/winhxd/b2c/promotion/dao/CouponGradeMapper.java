package com.winhxd.b2c.promotion.dao;

import com.winhxd.b2c.common.domain.promotion.condition.CouponGradeCondition;
import com.winhxd.b2c.common.domain.promotion.model.CouponGrade;
import com.winhxd.b2c.common.domain.promotion.vo.CouponGradeVO;
import com.winhxd.b2c.common.domain.promotion.vo.GradeTempleteCountVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CouponGradeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CouponGrade record);

    int insertSelective(CouponGrade record);

    CouponGrade selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CouponGrade record);

    int updateByPrimaryKey(CouponGrade record);

    CouponGradeVO viewCouponGradeDetail(@Param("id") long id);

    int updateCouponGradeValid(@Param("id")long id,@Param("userId") long userId,@Param("userName") String userName);

    long insertCouponGrade(@Param("couponGrade") CouponGrade couponGrade);

    List<CouponGradeVO> getCouponGradePage(@Param("condition")CouponGradeCondition condition);

    List<GradeTempleteCountVO> getGradeTempleteCountPage(@Param("gradeId") long gradeId);

}