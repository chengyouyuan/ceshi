package com.winhxd.b2c.promotion.service.impl;

import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.promotion.condition.CouponActivityCondition;
import com.winhxd.b2c.common.domain.promotion.vo.CouponActivityVO;
import com.winhxd.b2c.promotion.dao.CouponActivityMapper;
import com.winhxd.b2c.promotion.service.CouponActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author sjx
 * @date 2018/8/6
 */
@Service
public class CouponActivityServiceImpl implements CouponActivityService {

    @Autowired
    private CouponActivityMapper couponActivityMapper;

    @Override
    public PagedList<CouponActivityVO> queryPullCouponActivity(CouponActivityCondition condition) {
        return null;
    }
}
