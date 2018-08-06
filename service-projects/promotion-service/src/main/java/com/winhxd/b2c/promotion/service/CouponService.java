package com.winhxd.b2c.promotion.service;

import com.winhxd.b2c.common.domain.promotion.condition.CouponCondition;
import com.winhxd.b2c.common.domain.promotion.vo.CouponVO;

import java.util.List;

/**
 * @Auther wangxiaoshun
 * @Date 2018/8/6 13 58
 * @Description
 */
public interface CouponService {
    /**
     * 新人专享优惠列表
     *
     * @author pangjianhua
     * @param couponCondition 入参
     * @return
     */
    List<CouponVO> getNewUserCouponList(CouponCondition couponCondition);
}
