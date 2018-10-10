package com.winhxd.b2c.promotion.service;

import com.winhxd.b2c.common.domain.promotion.condition.CouponPushCondition;
import com.winhxd.b2c.common.domain.promotion.vo.CouponPushVO;

import java.util.List;

public interface CouponPushService {

    /**
     * c端用户获取指定推送优惠券
     * @return@Autowired
     */
    List<CouponPushVO> getSpecifiedPushCoupon(CouponPushCondition condition);

    /**
     * 判断是否有可领取优惠券
     * @param customerId
     * @return
     */
    boolean getAvailableCoupon(Long customerId);

    /**
     * 通过活动ID获取指定的用户的ID
     * @param activeId
     * @return
     */
    List<Long> getCustomerIdByActiveId(Long activeId);

}
