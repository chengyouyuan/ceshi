package com.winhxd.b2c.promotion.service;

import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.promotion.condition.*;
import com.winhxd.b2c.common.domain.promotion.vo.CouponDiscountVO;
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
     * @param customerId
     * @return
     */
    ResponseResult<String> getCouponNumsByCustomerForStore(Long customerId);

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
    List<CouponVO> couponListByOrder(OrderCouponCondition couponCondition);

    /**
     * 计算订单优惠金额
     * @param couponCondition
     * @return
     */
    CouponDiscountVO couponDiscountAmount(CouponPreAmountCondition couponCondition);

    /**
     * 校验优惠券是否可用
     * @param condition
     * @return
     */
    Boolean checkCouponStatus(CouponCheckStatusCondition condition);

    /**
     * 用户查询门店优惠券列表
     * @return
     */
    List<CouponVO> findStoreCouponList();

    /**
     * 订单可用优惠券
     * @param couponCondition
     * @return
     */
    List<CouponVO> availableCouponListByOrder(CouponPreAmountCondition couponCondition);
}
