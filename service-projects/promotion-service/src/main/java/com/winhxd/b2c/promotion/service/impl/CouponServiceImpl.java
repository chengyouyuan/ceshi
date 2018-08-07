package com.winhxd.b2c.promotion.service.impl;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.promotion.condition.CouponCondition;
import com.winhxd.b2c.common.domain.promotion.model.CouponActivity;
import com.winhxd.b2c.common.domain.promotion.model.CouponActivityDetail;
import com.winhxd.b2c.common.domain.promotion.model.CouponActivityRecord;
import com.winhxd.b2c.common.domain.promotion.model.CouponTemplateSend;
import com.winhxd.b2c.common.domain.promotion.vo.CouponVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.promotion.dao.CouponActivityDetailMapper;
import com.winhxd.b2c.promotion.dao.CouponActivityMapper;
import com.winhxd.b2c.promotion.dao.CouponActivityRecordMapper;
import com.winhxd.b2c.promotion.dao.CouponTemplateSendMapper;
import com.winhxd.b2c.promotion.service.CouponService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Auther wangxiaoshun
 * @Date 2018/8/6 13 59
 * @Description
 */
@Service
public class CouponServiceImpl implements CouponService {
    private static final Logger logger = LoggerFactory.getLogger(CouponServiceImpl.class);

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
        if(null == couponCondition.getCustomerId()){
            throw new NullPointerException("用户id不能为空");
        }
        //TODO 根据用户id 获取用户信息
        //step1 查询符合
        CouponActivity couponActivity = new CouponActivity();
        couponActivity.setCouponType((short)1);
        couponActivity.setStatus((short)1);
        List<CouponActivity> couponActivities = couponActivityMapper.selectByExample(couponActivity);
        if(couponActivities.isEmpty()){
            logger.error("不存在符合新用户注册的优惠券活动");
            throw new BusinessException(BusinessCode.CODE_500001);
        }

        CouponActivityRecord activityRecord = new CouponActivityRecord();
        activityRecord.setCustomerId(couponCondition.getCustomerId());
        activityRecord.setCouponActivityId(couponActivities.get(0).getId());
        List<CouponActivityRecord> couponActivityRecords = couponActivityRecordMapper.selectByExample(activityRecord);
        if(!couponActivityRecords.isEmpty()){
            logger.error("该手机号已经享受过新用户福利");
            throw new BusinessException(BusinessCode.CODE_500002);
        }

        CouponActivityDetail couponActivityDetail = new CouponActivityDetail();
        couponActivityDetail.setCouponActivityId(couponActivities.get(0).getId());
        List<CouponActivityDetail> couponActivityDetails = couponActivityDetailMapper.selectByExample(couponActivityDetail);
        if(couponActivityDetails.isEmpty()){
            logger.error("不存在符合新用户注册的优惠券活动");
            throw new BusinessException(BusinessCode.CODE_500001);
        }
        //step2 用户领券
        for(CouponActivityDetail activityDetail : couponActivityDetails){

            CouponTemplateSend couponTemplateSend = new CouponTemplateSend();
            couponTemplateSend.setStatus((short)2);
            couponTemplateSend.setTemplateId(activityDetail.getTemplateId());
            couponTemplateSend.setSource(1);
            couponTemplateSend.setSendRole(1);
            couponTemplateSend.setCustomerId(couponCondition.getCustomerId());
            couponTemplateSend.setCustomerMobile("");
            couponTemplateSend.setStartTime(activityDetail.getStartTime());
            couponTemplateSend.setEndTime(activityDetail.getEndTime());
            couponTemplateSend.setCount(couponActivities.get(0).getSendNum());
            couponTemplateSend.setCreatedBy(couponCondition.getCustomerId());
            couponTemplateSend.setCreated(new Date());
            couponTemplateSend.setCreatedByName("");
            couponTemplateSendMapper.insertSelective(couponTemplateSend);

            CouponActivityRecord couponActivityRecord = new CouponActivityRecord();
            couponActivityRecord.setCouponActivityId(activityDetail.getCouponActivityId());
            couponActivityRecord.setCustomerId(couponCondition.getCustomerId());
            couponActivityRecord.setSendId(couponTemplateSend.getId());
            couponActivityRecord.setTemplateId(activityDetail.getTemplateId());
            couponActivityRecord.setCreated(new Date());
            couponActivityRecord.setCreatedBy(couponCondition.getCustomerId());
            couponActivityRecord.setCreatedByName("");
            couponActivityRecordMapper.insertSelective(couponActivityRecord);
        }
        //step3 返回数据



        return null;
    }
}
