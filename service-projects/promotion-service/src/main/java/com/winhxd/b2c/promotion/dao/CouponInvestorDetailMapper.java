package com.winhxd.b2c.promotion.dao;

import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.promotion.model.CouponInvestorDetail;
import com.winhxd.b2c.common.domain.promotion.vo.CouponInvestorVO;
import org.apache.ibatis.annotations.Param;

public interface CouponInvestorDetailMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CouponInvestorDetail record);

    int insertSelective(CouponInvestorDetail record);

    CouponInvestorDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CouponInvestorDetail record);

    int updateByPrimaryKey(CouponInvestorDetail record);

    void updateCouponInvestorDetailByInvetorId(CouponInvestorDetail detail);

    void deleteCouponInvestorDetailByInvetorId(@Param("investorId") long investorId);
}