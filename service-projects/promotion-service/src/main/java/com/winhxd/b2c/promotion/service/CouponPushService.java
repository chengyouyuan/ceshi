package com.winhxd.b2c.promotion.service;

import com.winhxd.b2c.common.domain.promotion.vo.CouponPushVO;

import java.util.List;

public interface CouponPushService {

    /**
     * c端用户获取指定推送优惠券
     * @return@Autowired
     */
    List<CouponPushVO> getSpecifiedPushCoupon();
}
