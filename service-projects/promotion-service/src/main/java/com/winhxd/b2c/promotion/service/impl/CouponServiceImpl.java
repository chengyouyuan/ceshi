package com.winhxd.b2c.promotion.service.impl;

import com.winhxd.b2c.common.domain.promotion.condition.CouponCondition;
import com.winhxd.b2c.common.domain.promotion.vo.CouponVO;
import com.winhxd.b2c.promotion.service.CouponService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Auther wangxiaoshun
 * @Date 2018/8/6 13 59
 * @Description
 */
@Service
public class CouponServiceImpl implements CouponService {

    @Override
    public List<CouponVO> getNewUserCouponList(CouponCondition couponCondition) {
        return null;
    }
}
