package com.winhxd.b2c.promotion.dao;

import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.promotion.model.CouponInvestor;
import com.winhxd.b2c.common.domain.promotion.vo.CouponInvestorVO;
import org.apache.ibatis.annotations.Param;

public interface CouponInvestorMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CouponInvestor record);

    int insertSelective(CouponInvestor record);

    CouponInvestor selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CouponInvestor record);

    int updateByPrimaryKey(CouponInvestor record);

    Long insertCouponInvestor(CouponInvestor couponInvestor);

    CouponInvestorVO selectCouponInvestorDetailById(@Param("id") Long id);

    int updateCouponInvestorToValid(@Param("id")long id);

    void updateCouponInvestor(CouponInvestor couponInvestor);
}