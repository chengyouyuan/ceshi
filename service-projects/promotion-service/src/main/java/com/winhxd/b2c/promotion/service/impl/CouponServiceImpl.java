package com.winhxd.b2c.promotion.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.promotion.condition.CouponCondition;
import com.winhxd.b2c.common.domain.promotion.model.CouponActivity;
import com.winhxd.b2c.common.domain.promotion.model.CouponActivityDetail;
import com.winhxd.b2c.common.domain.promotion.model.CouponActivityRecord;
import com.winhxd.b2c.common.domain.promotion.model.CouponTemplateSend;
import com.winhxd.b2c.common.domain.promotion.vo.CouponVO;
import com.winhxd.b2c.common.domain.system.login.model.StoreUserInfo;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.store.StoreServiceClient;
import com.winhxd.b2c.promotion.dao.CouponActivityDetailMapper;
import com.winhxd.b2c.promotion.dao.CouponActivityMapper;
import com.winhxd.b2c.promotion.dao.CouponActivityRecordMapper;
import com.winhxd.b2c.promotion.dao.CouponTemplateSendMapper;
import com.winhxd.b2c.promotion.service.CouponService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Resource
    StoreServiceClient storeServiceClient;

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
        couponActivity.setActivityStatus((short)1);
        couponActivity.setType((short)2);
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
        //step2 向用户推券
        for(CouponActivityDetail activityDetail : couponActivityDetails){
            //推送数量
            for(int i=0; i <couponActivities.get(0).getSendNum();i++){
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
                //TODO 用户名称
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
        }
        //step3 返回数据

        List<CouponVO> couponVOS = this.getCouponList(couponCondition.getCustomerId(),1,null);

        return couponVOS;
    }

    /**
     * 通过customer 获取已经领取的优惠券列表
     * @param customerId
     * @param couponType 1 新用户注册，2 老用户活动
     * @Param useStatus 使用状态 1
     * @return
     */
    public List<CouponVO> getCouponList(Long customerId,Integer couponType,Integer useStatus){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("customerId",customerId);
        map.put("couponType",couponType);
        map.put("useStatus",useStatus);
        List<CouponVO> couponVOS = couponActivityMapper.selectCouponList(map);

        //TODO 1.通过品牌code 查询品牌信息  2.通过品类code 查询品类信息 3.通过商品code 查询商品信息

        return couponVOS;
    }

	@Override
	public Integer getCouponNumsByCustomerForStore(Long storeId, Long customerId) {
		// TODO Auto-generated method stub
		return null;
	}

    /**
     * 待领取优惠券
     * @param couponCondition 入参
     * @return
     */
    @Override
    public PagedList<CouponVO> unclaimedCouponList(CouponCondition couponCondition) {
        if(null == couponCondition.getCustomerId()){
            throw new NullPointerException("用户id不能为空");
        }
        ResponseResult<StoreUserInfo> result = storeServiceClient.findStoreUserInfoByCustomerId(couponCondition.getCustomerId());
        StoreUserInfo storeUserInfo = result.getData();
        //TODO Auto-generated method stub


        return null;
    }

    @Override
    public PagedList<CouponVO> myCouponList(CouponCondition couponCondition) {
        Page page = PageHelper.startPage(couponCondition.getPageNo(), couponCondition.getPageSize());
        PagedList<CouponVO> pagedList = new PagedList();
        List<CouponVO> couponVOS = this.getCouponList(couponCondition.getCustomerId(),null,couponCondition.getUseStatus());

        pagedList.setData(couponVOS);
        pagedList.setPageNo(couponCondition.getPageNo());
        pagedList.setPageSize(couponCondition.getPageSize());
        pagedList.setTotalRows(page.getTotal());
        return pagedList;
    }
}
