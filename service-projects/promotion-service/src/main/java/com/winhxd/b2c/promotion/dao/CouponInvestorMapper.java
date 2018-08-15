package com.winhxd.b2c.promotion.dao;

import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.promotion.condition.CouponInvestorCondition;
import com.winhxd.b2c.common.domain.promotion.model.CouponInvestor;
import com.winhxd.b2c.common.domain.promotion.vo.CouponInvestorVO;
import com.winhxd.b2c.common.domain.promotion.vo.InvertorTempleteCountVO;
import com.winhxd.b2c.common.domain.promotion.vo.TempleteRelationCountVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CouponInvestorMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CouponInvestor record);

    int insertSelective(CouponInvestor record);

    CouponInvestor selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CouponInvestor record);

    int updateByPrimaryKey(CouponInvestor record);

    Long insertCouponInvestor(CouponInvestor couponInvestor);

    CouponInvestorVO selectCouponInvestorDetailById(@Param("id") Long id);

    int updateCouponInvestorToValid(@Param("id")long id,@Param("userId")long userId,@Param("userName")String userName);

    void updateCouponInvestor(CouponInvestor couponInvestor);

    List<CouponInvestorVO> getCouponInvestorPage(@Param("condition") CouponInvestorCondition condition);

    List<InvertorTempleteCountVO> getInvertorTempleteCountPage(@Param("investorId") long investorId);

    TempleteRelationCountVO getRelationCouponInvCount(@Param("id") Long id);
}