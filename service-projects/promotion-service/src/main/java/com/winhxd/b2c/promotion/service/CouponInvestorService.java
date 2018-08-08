package com.winhxd.b2c.promotion.service;

import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.promotion.condition.CouponInvestorCondition;
import com.winhxd.b2c.common.domain.promotion.model.CouponInvestor;
import com.winhxd.b2c.common.domain.promotion.vo.CouponInvestorVO;

/**
 * @Author wl
 * @Date 2018/8/7 13:08
 * @Description
 **/
public interface CouponInvestorService {

    int saveCouponInvestor(CouponInvestorCondition condition);

    ResponseResult<CouponInvestorVO> getCouponInvestorDetailById(Long id);

    int updateCouponInvestorToValid(long id,long userId,String userName);

    int updateCouponInvestor(CouponInvestorCondition condition);

    ResponseResult<PagedList<CouponInvestorVO>> getCouponInvestorPage(CouponInvestorCondition condition);
}
