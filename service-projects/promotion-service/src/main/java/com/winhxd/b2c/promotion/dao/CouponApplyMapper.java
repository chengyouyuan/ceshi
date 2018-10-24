package com.winhxd.b2c.promotion.dao;

import com.winhxd.b2c.common.domain.promotion.condition.CouponApplyCondition;
import com.winhxd.b2c.common.domain.promotion.model.CouponApply;
import com.winhxd.b2c.common.domain.promotion.vo.ApplyTempleteCountVO;
import com.winhxd.b2c.common.domain.promotion.vo.CouponApplyVO;
import com.winhxd.b2c.common.domain.promotion.vo.TempleteRelationCountVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CouponApplyMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CouponApply record);

    int insertSelective(CouponApply record);

    CouponApply selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CouponApply record);

    int updateByPrimaryKey(CouponApply record);

    CouponApplyVO viewCouponApplyCommonDetail(@Param("id") long id);

    int updateCouponGradeValid(@Param("id")long id, @Param("userId")long userId, @Param("userName")String userName);

    List<CouponApplyVO> getCouponApplyPage(@Param("condition")CouponApplyCondition condition);

    long insertCouponApply(CouponApply couponApply);

    List<ApplyTempleteCountVO> findApplyTempleteCountPage(@Param("applyId") long applyId);

    TempleteRelationCountVO getRelationCouponApplyCount(@Param("id") Long id);

    List<Map<Long, Object>> getRelationCouponApplyCountMap(List<Long> ids);

    CouponApplyVO viewCouponApplyBrandDetail(@Param("id")long id);

    CouponApplyVO viewCouponApplyProdDetail(@Param("id") long id);

}