package com.winhxd.b2c.promotion.service;

import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.promotion.condition.*;
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
     * @author wangxiaoshun
     * @return
     */
    List<CouponVO> getNewUserCouponList();
    
    /**
     * @author liuhanning
     * @date  2018年8月7日 上午11:03:11
     * @Description 查询门店用户的领取优惠券的数量
     * @param storeId
     * @param customerId
     * @return
     */
    Integer getCouponNumsByCustomerForStore(Long storeId,Long customerId);

    /**
     * 待领取的优惠券列表
     *
     * @author wangxiaoshun
     * @return
     */
    List<CouponVO> unclaimedCouponList();

    /**
     * 我的优惠券列表
     * @author wangxiaoshun
     * @param couponCondition 入参
     * @return
     */
    PagedList<CouponVO> myCouponList(CouponCondition couponCondition);

    /**
     * 领取优惠券
     * @param condition
     * @return
     */
    Boolean userReceiveCoupon(ReceiveCouponCondition condition);

    /**
     * 订单使用优惠券
     * @param condition
     * @return
     */
    Boolean orderUseCoupon(OrderUseCouponCondition condition);

    /**
     * 订单退回优惠券
     * @param condition
     * @return
     */
    Boolean orderUntreadCoupon(OrderUntreadCouponCondition condition);

    /**
     * 撤回优惠券
     * @param condition
     * @return
     */
    Boolean revokeCoupon(RevokeCouponCodition condition);

    /**
     * 查询订单使用的优惠券列表
     * @param couponCondition
     * @return
     */
    PagedList<CouponVO> couponListByOrder(OrderCouponCondition couponCondition);
}
