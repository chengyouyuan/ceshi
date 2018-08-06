package com.winhxd.b2c.promotion.service;

import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.promotion.condition.CouponActivityCondition;
import com.winhxd.b2c.common.domain.promotion.vo.CouponActivityVO;

/**
 *
 * @author sjx
 * @date 2018/8/6
 */
public interface CouponActivityService {
    PagedList<CouponActivityVO> queryPullCouponActivity(CouponActivityCondition condition);
}
