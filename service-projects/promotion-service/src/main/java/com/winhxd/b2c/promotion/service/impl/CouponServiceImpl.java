package com.winhxd.b2c.promotion.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.context.CustomerUser;
import com.winhxd.b2c.common.context.UserContext;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.promotion.condition.CouponCondition;
import com.winhxd.b2c.common.domain.promotion.condition.ReceiveCouponCondition;
import com.winhxd.b2c.common.domain.promotion.enums.CouponActivityEnum;
import com.winhxd.b2c.common.domain.promotion.model.*;
import com.winhxd.b2c.common.domain.promotion.vo.CouponVO;
import com.winhxd.b2c.common.domain.system.login.model.StoreUserInfo;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.store.StoreServiceClient;
import com.winhxd.b2c.promotion.dao.*;
import com.winhxd.b2c.promotion.service.CouponService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

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
    @Autowired
    CouponActivityTemplateMapper couponActivityTemplateMapper;
    @Autowired
    CouponMapper couponMapper;
    @Resource
    StoreServiceClient storeServiceClient;

    @Override
    public List<CouponVO> getNewUserCouponList(CouponCondition couponCondition) {
        CustomerUser customerUser = UserContext.getCurrentCustomerUser();
        if (customerUser == null) {
            throw new BusinessException(BusinessCode.CODE_410001, "用户信息异常");
        }
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
        activityRecord.setCustomerId(customerUser.getCustomerId());
        activityRecord.setCouponActivityId(couponActivities.get(0).getId());
        List<CouponActivityRecord> couponActivityRecords = couponActivityRecordMapper.selectByExample(activityRecord);
        if(!couponActivityRecords.isEmpty()){
            logger.error("该手机号已经享受过新用户福利");
            throw new BusinessException(BusinessCode.CODE_500002);
        }

        CouponActivityTemplate couponActivityTemplate = new CouponActivityTemplate();
        couponActivityTemplate.setCouponActivityId(couponActivities.get(0).getId());
        List<CouponActivityTemplate> couponActivityTemplates = couponActivityTemplateMapper.selectByExample(couponActivityTemplate);
        if(couponActivityTemplates.isEmpty()){
            logger.error("不存在符合新用户注册的优惠券活动");
            throw new BusinessException(BusinessCode.CODE_500001);
        }
        //step2 向用户推券
        for(CouponActivityTemplate activityTemplate : couponActivityTemplates){
            //推送数量
            for(int i=0; i <activityTemplate.getSendNum();i++){
                CouponTemplateSend couponTemplateSend = new CouponTemplateSend();
                couponTemplateSend.setStatus(CouponActivityEnum.NOT_USE.getCode());
                couponTemplateSend.setTemplateId(activityTemplate.getTemplateId());
                couponTemplateSend.setSource((int) CouponActivityEnum.SYSTEM.getCode());
                couponTemplateSend.setSendRole((int) CouponActivityEnum.ORDINARY_USER.getCode());
                couponTemplateSend.setCustomerId(customerUser.getCustomerId());
                couponTemplateSend.setCustomerMobile(customerUser.getCustomerMobile());
                couponTemplateSend.setStartTime(activityTemplate.getStartTime());
                couponTemplateSend.setEndTime(activityTemplate.getEndTime());
                couponTemplateSend.setCount(1);
                couponTemplateSend.setCreatedBy(customerUser.getCustomerId());
                couponTemplateSend.setCreated(new Date());
                //TODO 用户名称
                couponTemplateSend.setCreatedByName("");
                couponTemplateSendMapper.insertSelective(couponTemplateSend);

                CouponActivityRecord couponActivityRecord = new CouponActivityRecord();
                couponActivityRecord.setCouponActivityId(activityTemplate.getCouponActivityId());
                couponActivityRecord.setCustomerId(customerUser.getCustomerId());
                couponActivityRecord.setSendId(couponTemplateSend.getId());
                couponActivityRecord.setTemplateId(activityTemplate.getTemplateId());
                couponActivityRecord.setCreated(new Date());
                couponActivityRecord.setCreatedBy(customerUser.getCustomerId());
                couponActivityRecord.setCreatedByName("");
                couponActivityRecordMapper.insertSelective(couponActivityRecord);
            }
        }
        //step3 返回数据

        List<CouponVO> couponVOS = this.getCouponList(customerUser.getCustomerId(),1);

        return couponVOS;
    }

    /**
     * 通过customer 获取已经领取的优惠券列表
     * @param customerId
     * @param couponType 1 新用户注册，2 老用户活动
     * @Param useStatus 使用状态 1
     * @return
     */
    public List<CouponVO> getCouponList(Long customerId,Integer couponType){
        List<CouponVO> couponVOS = couponActivityMapper.selectCouponList(customerId,couponType);

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
    public List<CouponVO> unclaimedCouponList(CouponCondition couponCondition) {
        CustomerUser customerUser = UserContext.getCurrentCustomerUser();
        if (customerUser == null) {
            throw new BusinessException(BusinessCode.CODE_410001, "用户信息异常");
        }
        ResponseResult<StoreUserInfo> result = storeServiceClient.findStoreUserInfoByCustomerId(couponCondition.getCustomerId());
        StoreUserInfo storeUserInfo = result.getData();


        List<CouponVO> couponVOS = couponActivityMapper.selectUnclaimedCouponList(storeUserInfo.getStoreCustomerId());
        List<CouponVO> results = new ArrayList<>();
        for(CouponVO couponVO : couponVOS){
            //根据优惠券总数限制用户领取
            if(couponVO.getCouponNumType().equals(CouponActivityEnum.COUPON_SUM.getCode())){
                int templateNum = couponMapper.getCouponNumByTemplateId(couponVO.getActivityId(),couponVO.getTemplateId());
                if(templateNum < couponVO.getCouponNum()){
                    int userNum = couponMapper.getCouponNumByCustomerId(couponVO.getActivityId(),couponVO.getTemplateId(),storeUserInfo.getStoreCustomerId(),customerUser.getCustomerId());
                    if(userNum < couponVO.getLimitNum()){
                        couponVO.setReceiveStatus("0");
                    }else{
                        couponVO.setReceiveStatus("1");
                    }
                }else{
                    // 优惠券已领完
                    continue;
                }
            }
            //根据每个门店可领取的优惠券数量限制用户领取
            if(couponVO.getCouponNumType().equals(CouponActivityEnum.STORE_NUM.getCode())){
                int storeNum = couponMapper.getCouponNumByStoreId(couponVO.getActivityId(),couponVO.getTemplateId(),storeUserInfo.getStoreCustomerId());
                if(storeNum < couponVO.getCouponNum()){
                    int userNum = couponMapper.getCouponNumByCustomerId(couponVO.getActivityId(),couponVO.getTemplateId(),storeUserInfo.getStoreCustomerId(),customerUser.getCustomerId());
                    if(userNum < couponVO.getLimitNum()){
                        couponVO.setReceiveStatus("0");
                    }else{
                        couponVO.setReceiveStatus("1");
                    }
                }else{
                    // 当前门店优惠券已领完
                    continue;
                }
            }
            results.add(couponVO);
        }
        return results;
    }

    @Override
    public PagedList<CouponVO> myCouponList(CouponCondition couponCondition) {
        CustomerUser customerUser = UserContext.getCurrentCustomerUser();
        if (customerUser == null) {
            throw new BusinessException(BusinessCode.CODE_410001, "用户信息异常");
        }

        Page page = PageHelper.startPage(couponCondition.getPageNo(), couponCondition.getPageSize());
        PagedList<CouponVO> pagedList = new PagedList();
        List<CouponVO> couponVOS = this.getCouponList(customerUser.getCustomerId(),null);

        pagedList.setData(couponVOS);
        pagedList.setPageNo(couponCondition.getPageNo());
        pagedList.setPageSize(couponCondition.getPageSize());
        pagedList.setTotalRows(page.getTotal());
        return pagedList;
    }

    @Override
    public Boolean userReceiveCoupon(ReceiveCouponCondition condition) {

        CustomerUser customerUser = UserContext.getCurrentCustomerUser();
        if (customerUser == null) {
            throw new BusinessException(BusinessCode.CODE_410001, "用户信息异常");
        }

        CouponActivityTemplate couponActivityTemplate = new CouponActivityTemplate();
        couponActivityTemplate.setCouponActivityId(condition.getCouponActivityId());
        couponActivityTemplate.setTemplateId(condition.getTemplateId());
        List<CouponActivityTemplate> couponActivityTemplates = couponActivityTemplateMapper.selectByExample(couponActivityTemplate);
        if(couponActivityTemplates.isEmpty()){
            logger.error("不存在符合新用户注册的优惠券活动");
            throw new BusinessException(BusinessCode.CODE_500001);
        }

        CouponTemplateSend couponTemplateSend = new CouponTemplateSend();
        couponTemplateSend.setStatus(CouponActivityEnum.NOT_USE.getCode());
        couponTemplateSend.setTemplateId(condition.getTemplateId());
        couponTemplateSend.setSource((int) CouponActivityEnum.SYSTEM.getCode());
        couponTemplateSend.setSendRole((int) CouponActivityEnum.ORDINARY_USER.getCode());
        couponTemplateSend.setCustomerId(customerUser.getCustomerId());
        couponTemplateSend.setCustomerMobile(customerUser.getCustomerMobile());
        couponTemplateSend.setStartTime(couponActivityTemplates.get(0).getStartTime());
        couponTemplateSend.setEndTime(couponActivityTemplates.get(0).getEndTime());
        couponTemplateSend.setCount(1);
        couponTemplateSend.setCreatedBy(customerUser.getCustomerId());
        couponTemplateSend.setCreated(new Date());
        //TODO 用户名称
        couponTemplateSend.setCreatedByName("");
        couponTemplateSendMapper.insertSelective(couponTemplateSend);

        CouponActivityRecord couponActivityRecord = new CouponActivityRecord();
        couponActivityRecord.setCouponActivityId(condition.getCouponActivityId());
        couponActivityRecord.setCustomerId(customerUser.getCustomerId());
        couponActivityRecord.setSendId(couponTemplateSend.getId());
        couponActivityRecord.setTemplateId(condition.getTemplateId());
        couponActivityRecord.setCreated(new Date());
        couponActivityRecord.setCreatedBy(customerUser.getCustomerId());
        couponActivityRecord.setCreatedByName("");
        couponActivityRecordMapper.insertSelective(couponActivityRecord);
        return true;
    }

    @Override
    public Boolean updateCouponStatus(CouponCondition condition) {
        CustomerUser customerUser = UserContext.getCurrentCustomerUser();
        if (customerUser == null) {
            throw new BusinessException(BusinessCode.CODE_410001, "用户信息异常");
        }

        List<Long> sendIds = condition.getSendIds();
        Integer useStatus = condition.getUseStatus();
        if(sendIds.isEmpty() || null == useStatus || condition.getCouponPrice() ==null || null ==condition.getOrderNo()||null == condition.getOrderPrice()){
            throw new BusinessException(BusinessCode.CODE_1007);
        }

        for(int i =0 ;i<sendIds.size();i++){
            CouponTemplateSend couponTemplateSend = couponTemplateSendMapper.selectByPrimaryKey(sendIds.get(i));
            couponTemplateSend.setStatus(useStatus.shortValue());
            couponTemplateSend.setUpdated(new Date());
            couponTemplateSend.setUpdateBy(customerUser.getCustomerId());
            couponTemplateSend.setUpdatedByName("");
            couponTemplateSendMapper.updateByPrimaryKeySelective(couponTemplateSend);

            CouponTemplateUse couponTemplateUse = new CouponTemplateUse();
            couponTemplateUse.setSendId(couponTemplateSend.getId());
            couponTemplateUse.setTemplateId(couponTemplateSend.getTemplateId());
            couponTemplateUse.setOrderNo(condition.getOrderNo());
            couponTemplateUse.setCouponPrice(condition.getCouponPrice());
            couponTemplateUse.setOrderPrice(condition.getOrderPrice());
            couponTemplateUse.setCustomerId(customerUser.getCustomerId());
            couponTemplateUse.setCustomerMonbile(customerUser.getCustomerMobile());
            couponTemplateUse.setCreated(new Date());
            couponTemplateUse.setCreatedBy(customerUser.getCustomerId());
            couponTemplateUse.setCreatedByName("");
        }
        return true;
    }
}
