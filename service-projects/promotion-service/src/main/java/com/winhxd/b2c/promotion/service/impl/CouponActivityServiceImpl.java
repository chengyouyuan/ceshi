package com.winhxd.b2c.promotion.service.impl;

import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.promotion.condition.CouponActivityAddCondition;
import com.winhxd.b2c.common.domain.promotion.condition.CouponActivityCondition;
import com.winhxd.b2c.common.domain.promotion.enums.CouponActivityEnum;
import com.winhxd.b2c.common.domain.promotion.model.CouponActivity;
import com.winhxd.b2c.common.domain.promotion.model.CouponActivityTemplate;
import com.winhxd.b2c.common.domain.promotion.vo.CouponActivityVO;
import com.winhxd.b2c.promotion.dao.CouponActivityMapper;
import com.winhxd.b2c.promotion.service.CouponActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

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
    public PagedList<CouponActivityVO> queryCouponActivity(CouponActivityCondition condition) {
        return couponActivityMapper.queryCouponActivity(condition);
    }

    @Override
    public int saveCouponActivity(CouponActivityAddCondition condition) {
        //CouponActivity
        CouponActivity couponActivity = new CouponActivity();
        couponActivity.setName(condition.getName());
        couponActivity.setCode(getUUID());
        couponActivity.setExolian(condition.getExolian());
        couponActivity.setRemarks(condition.getRemarks());
        couponActivity.setActivityStart(condition.getActivityStart());
        couponActivity.setActivityEnd(condition.getActivityEnd());
        couponActivity.setActivityStatus(CouponActivityEnum.ACTIVITY_OPEN.getCode());
        couponActivity.setStatus(CouponActivityEnum.ACTIVITY_EFFICTIVE.getCode());
        couponActivity.setCreated(new Date());
        couponActivity.setCreatedBy(123456L);
        couponActivity.setCreatedByName("测试用户");
        //领券
        if(CouponActivityEnum.PULL_COUPON.getCode() == condition.getType()){
            couponActivity.setType(CouponActivityEnum.PULL_COUPON.getCode());
        }
        //推券
        if(CouponActivityEnum.PUSH_COUPON.getCode() == condition.getType()){
            couponActivity.setType(CouponActivityEnum.PUSH_COUPON.getCode());
            couponActivity.setCouponType(CouponActivityEnum.NEW_USER.getCode());
        }
        couponActivityMapper.insertSelective(couponActivity);

        //CouponActivityTemplate
        CouponActivityTemplate couponActivityTemplate = new CouponActivityTemplate();

        for (int i=0 ; i < condition.getCouponActivityTemplateList().size(); i++) {
            couponActivityTemplate.setCouponActivityId(couponActivity.getId());
            couponActivityTemplate.setTemplateId(condition.getCouponActivityTemplateList().get(i).getTemplateId());
        }




        return 0;
    }

    @Override
    public int updateCouponActivity(CouponActivityAddCondition condition) {
        return 0;
    }

    /**
     * 自动生成32位的UUid，对应数据库的主键id进行插入用。
     * @return
     */
    public String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
