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

    /**
     * @Deccription 查询优惠券领券推券列表
     * @param condition
     * @return CouponActivityVO
     */
    PagedList<CouponActivityVO> queryCouponActivity(CouponActivityCondition condition);
    /**
     *
     *@Deccription 添加领券活动
     *@Params  condition
     *@Return  ResponseResult
     *@User  sjx
     *@Date   2018/8/6
     */
    int savePullCouponActivity(CouponActivityCondition condition);
}
