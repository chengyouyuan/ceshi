package com.winhxd.b2c.promotion.service.impl;

import com.winhxd.b2c.common.domain.promotion.condition.CouponCondition;
import com.winhxd.b2c.common.domain.promotion.model.CouponActivity;
import com.winhxd.b2c.common.domain.promotion.model.CouponActivityDetail;
import com.winhxd.b2c.common.domain.promotion.vo.CouponVO;
import com.winhxd.b2c.promotion.dao.CouponActivityDetailMapper;
import com.winhxd.b2c.promotion.dao.CouponActivityMapper;
import com.winhxd.b2c.promotion.dao.CouponActivityRecordMapper;
import com.winhxd.b2c.promotion.dao.CouponTemplateSendMapper;
import com.winhxd.b2c.promotion.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Auther wangxiaoshun
 * @Date 2018/8/6 13 59
 * @Description
 */
@Service
public class CouponServiceImpl implements CouponService {
    @Autowired
    CouponActivityMapper couponActivityMapper;

    @Autowired
    CouponActivityDetailMapper couponActivityDetailMapper;

    @Autowired
    CouponActivityRecordMapper couponActivityRecordMapper;
    @Autowired
    CouponTemplateSendMapper couponTemplateSendMapper;

    @Override
    public List<CouponVO> getNewUserCouponList(CouponCondition couponCondition) {
        //TODO 根据用户id 获取用户信息
        //step1 查询符合
        CouponActivity couponActivity = new CouponActivity();
        couponActivity.setCouponType((short)1);
        couponActivity.setStatus((short)1);
        List<CouponActivity> couponActivities = couponActivityMapper.selectByExample(couponActivity);


        CouponActivityDetail couponActivityDetail = new CouponActivityDetail();
        couponActivityDetail.setCouponActivityId(couponActivities.get(0).getId());
        List<CouponActivityDetail> couponActivityDetails = couponActivityDetailMapper.selectByExample(couponActivityDetail);
        return null;
    }
}
