package com.winhxd.b2c.promotion.service.impl;

import com.winhxd.b2c.common.domain.promotion.vo.CouponPushVO;
import com.winhxd.b2c.promotion.dao.CouponPushCustomerMapper;
import com.winhxd.b2c.promotion.service.CouponPushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CouponPushServiceImpl implements CouponPushService {

    @Autowired
    CouponPushCustomerMapper couponPushCustomerMapper;

    @Override
    public List<CouponPushVO> getSpecifiedPushCoupon() {
        return null;
    }
}
