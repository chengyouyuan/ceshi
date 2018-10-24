package com.winhxd.b2c.promotion.dao;

import com.winhxd.b2c.common.domain.promotion.condition.CouponGradeCondition;
import com.winhxd.b2c.common.domain.promotion.model.CouponGrade;
import com.winhxd.b2c.common.domain.promotion.vo.CouponGradeVO;
import com.winhxd.b2c.common.domain.promotion.vo.GradeTempleteCountVO;
import com.winhxd.b2c.common.domain.promotion.vo.TempleteRelationCountVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CouponGradeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CouponGrade record);

    int insertSelective(CouponGrade record);

    CouponGrade selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CouponGrade record);

    int updateByPrimaryKey(CouponGrade record);

    CouponGradeVO viewCouponGradeDetail(@Param("id") Long id);

    int updateCouponGradeValid(@Param("id")Long id,@Param("userId") Long userId,@Param("userName") String userName);

    long insertCouponGrade( CouponGrade couponGrade);

    List<CouponGradeVO> getCouponGradePage(@Param("condition")CouponGradeCondition condition);

    List<GradeTempleteCountVO> getGradeTempleteCountPage(@Param("gradeId") Long gradeId);

    TempleteRelationCountVO getRelationCouponGradeCount(@Param("id") Long id);

    List<Map<Long, Object>> getRelationCouponGradeCountMap(List<Long> ids);

}