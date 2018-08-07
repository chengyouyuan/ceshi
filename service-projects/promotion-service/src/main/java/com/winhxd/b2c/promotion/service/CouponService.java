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
    
    /**
     * @author liuhanning
     * @date  2018年8月7日 上午11:03:11
     * @Description 查询门店用户的领取优惠券的数量
     * @param storeId
     * @param customerId
     * @return
     */
    Integer getCouponNumsByCustomerForStore(Long storeId,Long customerId);
    
}
