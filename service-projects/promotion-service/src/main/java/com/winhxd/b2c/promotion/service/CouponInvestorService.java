package com.winhxd.b2c.promotion.service;

import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.promotion.condition.CouponInvestorCondition;
import com.winhxd.b2c.common.domain.promotion.condition.RuleRealationCountCondition;
import com.winhxd.b2c.common.domain.promotion.model.CouponInvestor;
import com.winhxd.b2c.common.domain.promotion.vo.CouponInvestorVO;
import com.winhxd.b2c.common.domain.promotion.vo.InvertorTempleteCountVO;

/**
 * @Author wl
 * @Date 2018/8/7 13:08
 * @Description
 **/
public interface CouponInvestorService {

    int saveCouponInvestor(CouponInvestorCondition condition);

    ResponseResult<CouponInvestorVO> getCouponInvestorDetailById(Long id);

    int updateCouponInvestorToValid(long id,long userId,String userName);


    ResponseResult<PagedList<CouponInvestorVO>> getCouponInvestorPage(CouponInvestorCondition condition);

    ResponseResult<PagedList<InvertorTempleteCountVO>> findInvertorTempleteCountPage(RuleRealationCountCondition condition);
}
