package com.winhxd.b2c.promotion.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.promotion.condition.CouponActivityAddCondition;
import com.winhxd.b2c.common.domain.promotion.condition.CouponActivityCondition;
import com.winhxd.b2c.common.domain.promotion.condition.RevokeCouponCodition;
import com.winhxd.b2c.common.domain.promotion.enums.CouponActivityEnum;
import com.winhxd.b2c.common.domain.promotion.model.*;
import com.winhxd.b2c.common.domain.promotion.vo.CouponActivityStoreVO;
import com.winhxd.b2c.common.domain.promotion.vo.CouponActivityVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.promotion.dao.*;
import com.winhxd.b2c.promotion.service.CouponActivityService;
import com.winhxd.b2c.promotion.service.CouponService;

import io.swagger.annotations.ApiModelProperty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 *
 * @author sjx
 * @date 2018/8/6
 */
@Service
public class CouponActivityServiceImpl implements CouponActivityService {

    @Autowired
    private CouponActivityMapper couponActivityMapper;
    @Autowired
    private CouponActivityTemplateMapper couponActivityTemplateMapper;
    @Autowired
    private CouponActivityStoreCustomerMapper couponActivityStoreCustomerMapper;
    @Autowired
    private CouponActivityAreaMapper couponActivityAreaMapper;
    @Autowired
    private CouponService couponService;
    @Autowired
    private CouponActivityRecordMapper couponActivityRecordMapper;

    /**
     * 查询活动列表
     * @param condition
     */
    @Override
    public List<CouponActivityVO> findCouponActivity(CouponActivityCondition condition) {
        if(condition.getDateInterval() != null){
            if(condition.getDateInterval().getStart() != null){
                Calendar createdS = Calendar.getInstance();
                createdS.setTime(condition.getDateInterval().getStart());
                createdS.set(Calendar.HOUR_OF_DAY, 0);
                createdS.set(Calendar.MINUTE, 0);
                createdS.set(Calendar.SECOND, 0);
                createdS.set(Calendar.MILLISECOND, 0);
                Date createdStart = createdS.getTime();
                condition.setCreatedStart(createdStart);
            }
            if(condition.getDateInterval().getEnd() != null){
                Calendar createdE = Calendar.getInstance();
                createdE.setTime(condition.getDateInterval().getEnd());
                createdE.set(Calendar.HOUR_OF_DAY, 23);
                createdE.set(Calendar.MINUTE, 59);
                createdE.set(Calendar.SECOND, 59);
                createdE.set(Calendar.MILLISECOND, 59);
                Date createdEnd  =createdE.getTime();
                condition.setCreatedEnd(createdEnd);

            }
        }

        List<CouponActivityVO> activity = couponActivityMapper.selectCouponActivity(condition);
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(activity)) {
        	List<Long> activityIds=new ArrayList<>();
        	for (CouponActivityVO couponActivityVO : activity) {
				activityIds.add(couponActivityVO.getId());
			}
        	//查询门店数量
        	//获取领取、使用、撤销数量
        	List<Map<String, Object>> getNumList=couponActivityMapper.selectNums("",activityIds);
        	Map<Long, Object> getNumMap=getActivityMap(getNumList);
        	List<Map<String, Object>> userNumList=couponActivityMapper.selectNums("1",activityIds);
        	Map<Long, Object> userNumMap=getActivityMap(userNumList);
        	List<Map<String, Object>> revocationNumList=couponActivityMapper.selectNums("0",activityIds);
        	Map<Long, Object> revocationNumMap=getActivityMap(revocationNumList);

        	//重新组装list数据
        	for (CouponActivityVO couponActivityVO : activity) {
        		Long getNum=0L;
        		Long useNum=0L;
        		Long revocationNum=0L;
        		Long activityId=couponActivityVO.getId();
        		if (getNumMap.containsKey(activityId)) {
        			getNum=(Long) getNumMap.get(activityId);
				}
        		if (userNumMap.containsKey(activityId)) {
        			useNum=(Long) userNumMap.get(activityId);
        		}
        		if (revocationNumMap.containsKey(activityId)) {
        			revocationNum=(Long) revocationNumMap.get(activityId);
        		}
    		     couponActivityVO.setGetNum(getNum.intValue());
    		     couponActivityVO.setUseNum(useNum.intValue());
    		     couponActivityVO.setRevocationNum(revocationNum.intValue());
			}
		}
        
        
        return activity;
    }

	public Map<Long, Object> getActivityMap(List<Map<String, Object>> list) {
		Map<Long, Object> activityMap = new HashMap<>();
		if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(list)) {
			for (Map<String, Object> map : list) {
				Long activityId=(Long) map.get("activityId");
				Long value=(Long) map.get("numValue");
				activityMap.put(activityId, value);
			}
		}
		return activityMap;
	}

    /**
     * 新增活动
     * @param condition
     */
    @Override
    @Transactional
    public void saveCouponActivity(CouponActivityAddCondition condition) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(condition.getActivityStart());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Calendar c = Calendar.getInstance();
        c.setTime(condition.getActivityEnd());
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 59);
        Date activityStart = calendar.getTime();
        Date activityEnd  =c.getTime();
        //CouponActivity
        CouponActivity couponActivity = new CouponActivity();
        couponActivity.setName(condition.getName());
        couponActivity.setCode(condition.getCode());
        couponActivity.setExolian(condition.getExolian());
        couponActivity.setRemarks(condition.getRemarks());
        couponActivity.setActivityStart(activityStart);
        couponActivity.setActivityEnd(activityEnd);
        couponActivity.setActivityStatus(CouponActivityEnum.ACTIVITY_OPEN.getCode());
        couponActivity.setStatus(CouponActivityEnum.ACTIVITY_EFFICTIVE.getCode());
        couponActivity.setCreated(new Date());
        couponActivity.setCreatedBy(condition.getCreatedBy());
        couponActivity.setCreatedByName(condition.getCreatedByName());
        //领券
        if(CouponActivityEnum.PULL_COUPON.getCode() == condition.getType()){
            couponActivity.setType(CouponActivityEnum.PULL_COUPON.getCode());
        }
        //推券
        if(CouponActivityEnum.PUSH_COUPON.getCode() == condition.getType()){
            couponActivity.setType(CouponActivityEnum.PUSH_COUPON.getCode());
            couponActivity.setCouponType(CouponActivityEnum.NEW_USER.getCode());
        }
        int n = couponActivityMapper.insertSelective(couponActivity);
        if(n==0){
            throw new BusinessException(BusinessCode.CODE_503001,"优惠券活动添加失败");
        }
        //获取区域信息
        if(!CollectionUtils.isEmpty(condition.getCouponActivityAreaList())){
            for (int a = 0 ; a < condition.getCouponActivityAreaList().size(); a++){
                CouponActivityArea couponActivityArea = new CouponActivityArea();
                couponActivityArea.setCouponActivityId(couponActivity.getId());
                couponActivityArea.setRegionCode(condition.getCouponActivityAreaList().get(a).getRegionCode());
                couponActivityArea.setRegionName(condition.getCouponActivityAreaList().get(a).getRegionName());
                couponActivityArea.setStatus(CouponActivityEnum.ACTIVITY_EFFICTIVE.getCode());
                int n4 = couponActivityAreaMapper.insertSelective(couponActivityArea);
                if(n4==0){
                    throw new BusinessException(BusinessCode.CODE_503001,"优惠券活动添加失败");
                }
            }
        }

        //CouponActivityTemplate
        for (int i=0 ; i < condition.getCouponActivityTemplateList().size(); i++) {
            CouponActivityTemplate couponActivityTemplate = new CouponActivityTemplate();
            couponActivityTemplate.setCouponActivityId(couponActivity.getId());
            couponActivityTemplate.setTemplateId(condition.getCouponActivityTemplateList().get(i).getTemplateId());
            couponActivityTemplate.setStatus(CouponActivityEnum.ACTIVITY_EFFICTIVE.getCode());
            //领券
            if(CouponActivityEnum.PULL_COUPON.getCode() == condition.getType()){
                Calendar couponS = Calendar.getInstance();
                couponS.setTime(condition.getCouponActivityTemplateList().get(i).getStartTime());
                couponS.set(Calendar.HOUR_OF_DAY, 0);
                couponS.set(Calendar.MINUTE, 0);
                couponS.set(Calendar.SECOND, 0);
                couponS.set(Calendar.MILLISECOND, 0);

                Calendar couponE = Calendar.getInstance();
                couponE.setTime(condition.getCouponActivityTemplateList().get(i).getEndTime());
                couponE.set(Calendar.HOUR_OF_DAY, 23);
                couponE.set(Calendar.MINUTE, 59);
                couponE.set(Calendar.SECOND, 59);
                couponE.set(Calendar.MILLISECOND, 59);
                Date couponStart = couponS.getTime();
                Date couponEnd  =couponE.getTime();
                couponActivityTemplate.setStartTime(couponStart);
                couponActivityTemplate.setEndTime(couponEnd);
                couponActivityTemplate.setCouponNumType(condition.getCouponActivityTemplateList().get(i).getCouponNumType());
                couponActivityTemplate.setCouponNum(condition.getCouponActivityTemplateList().get(i).getCouponNum());
                couponActivityTemplate.setCustomerVoucherLimitType(condition.getCouponActivityTemplateList().get(i).getCustomerVoucherLimitType());
                if(condition.getCouponActivityTemplateList().get(i).getCustomerVoucherLimitType() == CouponActivityEnum.STORE_LIMITED.getCode()){
                    couponActivityTemplate.setCustomerVoucherLimitNum(condition.getCouponActivityTemplateList().get(i).getCustomerVoucherLimitNum());
                }
                //如果不限制则最大张数限定100张
                if(condition.getCouponActivityTemplateList().get(i).getCustomerVoucherLimitType() == CouponActivityEnum.UNLIMITED.getCode()){
                    couponActivityTemplate.setCustomerVoucherLimitNum(100);
                }
            }
            //推券
            if(CouponActivityEnum.PUSH_COUPON.getCode() == condition.getType()){
                couponActivityTemplate.setEffectiveDays(condition.getCouponActivityTemplateList().get(i).getEffectiveDays());
                couponActivityTemplate.setSendNum(condition.getCouponActivityTemplateList().get(i).getSendNum());
            }
            int n2 = couponActivityTemplateMapper.insertSelective(couponActivityTemplate);
            if(n2==0){
                throw new BusinessException(BusinessCode.CODE_503001,"优惠券活动添加失败");
            }

            if(CouponActivityEnum.PULL_COUPON.getCode() == condition.getType()){
                //coupon_activity_store_customer
                for (int j=0 ; j < condition.getCouponActivityTemplateList().get(i).getCouponActivityStoreCustomerList().size(); j++) {
                    CouponActivityStoreCustomer couponActivityStoreCustomer  = new CouponActivityStoreCustomer();
                    couponActivityStoreCustomer.setCouponActivityTemplateId(couponActivityTemplate.getId());
                    couponActivityStoreCustomer.setStoreId(condition.getCouponActivityTemplateList().get(i).getCouponActivityStoreCustomerList().get(j).getStoreId());
                    couponActivityStoreCustomer.setCustomerId(condition.getCouponActivityTemplateList().get(i).getCouponActivityStoreCustomerList().get(j).getCustomerId());
                    couponActivityStoreCustomer.setStatus(CouponActivityEnum.ACTIVITY_EFFICTIVE.getCode());
                    int n3 = couponActivityStoreCustomerMapper.insertSelective(couponActivityStoreCustomer);
                    if(n3==0){
                        throw new BusinessException(BusinessCode.CODE_503001,"优惠券活动添加失败");
                    }
                }
            }
        }
    }

    /**
     *
     *@Deccription 优惠券活动查看和回显编辑页
     *@Params  condition
     *@Return  ResponseResult
     *@User  sjx
     *@Date   2018/8/8
     */
    @Override
    public CouponActivityVO getCouponActivityById(String id) {
        CouponActivityVO couponActivityVO = new CouponActivityVO();
        List<CouponActivityStoreCustomer> couponActivityStoreCustomerList;

        CouponActivity couponActivity = couponActivityMapper.selectByPrimaryKey(Long.valueOf(id));
        couponActivityVO.setId(couponActivity.getId());
        couponActivityVO.setName(couponActivity.getName());
        couponActivityVO.setCode(couponActivity.getCode());
        couponActivityVO.setExolian(couponActivity.getExolian());
        couponActivityVO.setRemarks(couponActivity.getRemarks());
        couponActivityVO.setType(couponActivity.getType());
        couponActivityVO.setActivityStart(couponActivity.getActivityStart());
        couponActivityVO.setActivityEnd(couponActivity.getActivityEnd());
        couponActivityVO.setActivityStatus(couponActivity.getActivityStatus());
        if(couponActivity.getType() == CouponActivityEnum.PUSH_COUPON.getCode()){
            couponActivityVO.setCouponType(couponActivity.getCouponType());
        }
        //优惠券信息
        List<CouponActivityTemplate> couponActivityTemplateList = couponActivityTemplateMapper.selectTemplateByActivityId(couponActivity.getId());
        if (!CollectionUtils.isEmpty(couponActivityTemplateList)){
            couponActivityVO.setCouponActivityTemplateList(couponActivityTemplateList);
        }
        //区域信息
        List<CouponActivityArea> couponActivityAreaList = couponActivityAreaMapper.selectAreaByActivityId(couponActivity.getId());
        if (!CollectionUtils.isEmpty(couponActivityAreaList)){
            couponActivityVO.setCouponActivityAreaList(couponActivityAreaList);
        }
        //门店或用户信息
        for(int i = 0 ; i < couponActivityTemplateList.size() ; i++) {
            couponActivityStoreCustomerList = couponActivityStoreCustomerMapper.selectByTemplateId(couponActivityTemplateList.get(i).getId());
            if (!CollectionUtils.isEmpty(couponActivityStoreCustomerList)){
                couponActivityVO.getCouponActivityTemplateList().get(i).setCouponActivityStoreCustomerList(couponActivityStoreCustomerList);
            }
        }
        return couponActivityVO;
    }

    /**
     * 编辑活动
     * @param condition
     */
    @Override
    @Transactional
    public void updateCouponActivity(CouponActivityAddCondition condition) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(condition.getActivityStart());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Calendar c = Calendar.getInstance();
        c.setTime(condition.getActivityEnd());
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 59);
        Date activityStart = calendar.getTime();
        Date activityEnd  =c.getTime();
        //更新CouponActivity
        CouponActivity couponActivity = new CouponActivity();
        couponActivity.setId(condition.getId());
        couponActivity.setName(condition.getName());
        couponActivity.setCode(condition.getCode());
        couponActivity.setExolian(condition.getExolian());
        couponActivity.setRemarks(condition.getRemarks());
        couponActivity.setActivityStart(activityStart);
        couponActivity.setActivityEnd(activityEnd);
        couponActivity.setActivityStatus(CouponActivityEnum.ACTIVITY_OPEN.getCode());
        couponActivity.setStatus(CouponActivityEnum.ACTIVITY_EFFICTIVE.getCode());
        couponActivity.setUpdated(new Date());
        couponActivity.setUpdatedBy(condition.getCreatedBy());
        couponActivity.setUpdatedByName(condition.getCreatedByName());
        //领券
        if(CouponActivityEnum.PULL_COUPON.getCode() == condition.getType()){
            couponActivity.setType(CouponActivityEnum.PULL_COUPON.getCode());
        }
        //推券
        if(CouponActivityEnum.PUSH_COUPON.getCode() == condition.getType()){
            couponActivity.setType(CouponActivityEnum.PUSH_COUPON.getCode());
            couponActivity.setCouponType(CouponActivityEnum.NEW_USER.getCode());
        }
        int n = couponActivityMapper.updateByPrimaryKeySelective(couponActivity);
        if(n==0){
            throw new BusinessException(BusinessCode.CODE_503201,"优惠券活动更新失败");
        }

        //删除CouponActivityTemplate
        CouponActivityTemplate cat = new CouponActivityTemplate();
        cat.setCouponActivityId(condition.getId());
        cat.setStatus(CouponActivityEnum.ACTIVITY_INVALID.getCode());
        int n2 = couponActivityTemplateMapper.updateByCouponActivityId(cat);
        if(n2==0){
            throw new BusinessException(BusinessCode.CODE_503201,"优惠券活动更新失败");
        }
        CouponActivityStoreCustomer casc = new CouponActivityStoreCustomer();
        if(CouponActivityEnum.PULL_COUPON.getCode() == condition.getType()){
            //删除couponActivityStoreCustomer
            casc.setCouponActivityTemplateId(condition.getCouponActivityTemplateList().get(0).getId());
            casc.setStatus(CouponActivityEnum.ACTIVITY_INVALID.getCode());
            int n3 = couponActivityStoreCustomerMapper.updateByCouponActivityTemplateId(casc);
            if(n3==0){
                throw new BusinessException(BusinessCode.CODE_503201,"优惠券活动更新失败");
            }
        }

        //新增couponActivityTemplate
        for (int i=0 ; i < condition.getCouponActivityTemplateList().size(); i++) {
            CouponActivityTemplate couponActivityTemplate = new CouponActivityTemplate();
            couponActivityTemplate.setCouponActivityId(couponActivity.getId());
            couponActivityTemplate.setStatus(CouponActivityEnum.ACTIVITY_EFFICTIVE.getCode());
            couponActivityTemplate.setTemplateId(condition.getCouponActivityTemplateList().get(i).getTemplateId());
            //领券
            if(CouponActivityEnum.PULL_COUPON.getCode() == condition.getType()){
                Calendar couponS = Calendar.getInstance();
                couponS.setTime(condition.getCouponActivityTemplateList().get(i).getStartTime());
                couponS.set(Calendar.HOUR_OF_DAY, 0);
                couponS.set(Calendar.MINUTE, 0);
                couponS.set(Calendar.SECOND, 0);
                couponS.set(Calendar.MILLISECOND, 0);

                Calendar couponE = Calendar.getInstance();
                couponE.setTime(condition.getCouponActivityTemplateList().get(i).getEndTime());
                couponE.set(Calendar.HOUR_OF_DAY, 23);
                couponE.set(Calendar.MINUTE, 59);
                couponE.set(Calendar.SECOND, 59);
                couponE.set(Calendar.MILLISECOND, 59);
                Date couponStart = couponS.getTime();
                Date couponEnd  =couponE.getTime();
                couponActivityTemplate.setStartTime(couponStart);
                couponActivityTemplate.setEndTime(couponEnd);
                couponActivityTemplate.setCouponNumType(condition.getCouponActivityTemplateList().get(i).getCouponNumType());
                couponActivityTemplate.setCouponNum(condition.getCouponActivityTemplateList().get(i).getCouponNum());
                couponActivityTemplate.setCustomerVoucherLimitType(condition.getCouponActivityTemplateList().get(i).getCustomerVoucherLimitType());
                if(condition.getCouponActivityTemplateList().get(i).getCustomerVoucherLimitType() == CouponActivityEnum.STORE_LIMITED.getCode()){
                    couponActivityTemplate.setCustomerVoucherLimitNum(condition.getCouponActivityTemplateList().get(i).getCustomerVoucherLimitNum());
                }
                //如果不限制则最大张数限定100张
                if(condition.getCouponActivityTemplateList().get(i).getCustomerVoucherLimitType() == CouponActivityEnum.UNLIMITED.getCode()){
                    couponActivityTemplate.setCustomerVoucherLimitNum(100);
                }
            }
            //推券
            if(CouponActivityEnum.PUSH_COUPON.getCode() == condition.getType()){
                couponActivityTemplate.setEffectiveDays(condition.getCouponActivityTemplateList().get(i).getEffectiveDays());
                couponActivityTemplate.setSendNum(condition.getCouponActivityTemplateList().get(i).getSendNum());
            }
            int n4 =couponActivityTemplateMapper.insertSelective(couponActivityTemplate);
            if(n4==0){
                throw new BusinessException(BusinessCode.CODE_503201,"优惠券活动更新失败");
            }

            if(CouponActivityEnum.PULL_COUPON.getCode() == condition.getType()){
                for (int j=0 ; j < condition.getCouponActivityTemplateList().get(i).getCouponActivityStoreCustomerList().size(); j++) {
                    //新增couponActivityStoreCustomer
                    CouponActivityStoreCustomer couponActivityStoreCustomer = new CouponActivityStoreCustomer();
                    couponActivityStoreCustomer.setCouponActivityTemplateId(couponActivityTemplate.getId());
                    couponActivityStoreCustomer.setStoreId(condition.getCouponActivityTemplateList().get(i).getCouponActivityStoreCustomerList().get(j).getStoreId());
                    couponActivityStoreCustomer.setCustomerId(condition.getCouponActivityTemplateList().get(i).getCouponActivityStoreCustomerList().get(j).getCustomerId());
                    couponActivityStoreCustomer.setStatus(CouponActivityEnum.ACTIVITY_EFFICTIVE.getCode());
                    int n5 = couponActivityStoreCustomerMapper.insertSelective(couponActivityStoreCustomer);
                    if(n5==0){
                        throw new BusinessException(BusinessCode.CODE_503201,"优惠券活动更新失败");
                    }
                }
            }
        }
    }


    /**
     * 删除活动信息（更新活动状态为无效）
     * @param condition
     */
    @Override
    @Transactional
    public void deleteCouponActivity(CouponActivityCondition condition) {
        CouponActivity couponActivity = new CouponActivity();
        couponActivity.setId(condition.getId());
        couponActivity.setStatus(CouponActivityEnum.ACTIVITY_INVALID.getCode());
        couponActivity.setUpdated(new Date());
        couponActivity.setUpdatedBy(condition.getCreatedBy());
        couponActivity.setUpdatedByName(condition.getCreatedByName());
        int n = couponActivityMapper.updateByPrimaryKeySelective(couponActivity);
        if(n==0){
            throw new BusinessException(BusinessCode.CODE_503301,"删除活动信息失败");
        }
    }

    /**
     * 停止活动，撤销优惠券
     * @param condition
     */
    @Override
    @Transactional
    public void revocationActivityCoupon(CouponActivityCondition condition) {
        //停止活动
        CouponActivity couponActivity = new CouponActivity();
        couponActivity.setId(condition.getId());
        couponActivity.setActivityStatus(CouponActivityEnum.ACTIVITY_STOP.getCode());
        couponActivity.setUpdated(new Date());
        couponActivity.setUpdatedBy(condition.getCreatedBy());
        couponActivity.setUpdatedByName(condition.getCreatedByName());

        int n = couponActivityMapper.updateByPrimaryKeySelective(couponActivity);
        if(n==0){
            throw new BusinessException(BusinessCode.CODE_503501,"停止活动失败");
        }
        List<CouponActivityRecord> recordList = couponActivityRecordMapper.selectRecordByActivityId(condition.getId());
        if (CollectionUtils.isEmpty(recordList)){
            throw new BusinessException(BusinessCode.CODE_503401,"撤销活动优惠券失败");
        }
        //撤销已发放的优惠券
        List<Long> longList = new ArrayList<>();
        for (int i=0;i<recordList.size();i++){
            longList.add(recordList.get(i).getSendId());
        }
        RevokeCouponCodition couponCondition = new RevokeCouponCodition();
        couponCondition.setSendIds(longList);
        couponService.revokeCoupon(couponCondition);
    }

    /**
     * 开启/停止活动
     * @param condition
     */
    @Override
    @Transactional
    public void updateCouponActivityStatus(CouponActivityAddCondition condition) {
        //更新CouponActivity
        CouponActivity couponActivity = new CouponActivity();
        couponActivity.setId(condition.getId());
        couponActivity.setActivityStatus(condition.getActivityStatus());
        couponActivity.setUpdated(new Date());
        couponActivity.setUpdatedBy(condition.getCreatedBy());
        couponActivity.setUpdatedByName(condition.getCreatedByName());

        int n = couponActivityMapper.updateByPrimaryKeySelective(couponActivity);
        if(n==0){
            throw new BusinessException(BusinessCode.CODE_503501,"停止活动失败");
        }
    }
    /**
     * 根据活动查询优惠券信息
     * @param condition
     */
    @Override
    public List<CouponActivityStoreVO> findCouponByActivity(CouponActivityCondition condition) {
        List<CouponActivityStoreVO> coupon = couponActivityMapper.selectCouponByActivity(condition);
        return coupon;
    }

    /**
     * 根据活动查询小店信息
     * @param condition
     */
    @Override
    public List<CouponActivityStoreVO> findStoreByActivity(CouponActivityCondition condition) {
        List<CouponActivityStoreVO> store = couponActivityMapper.selectStoreByActivity(condition);
        return store;
    }

    @Override
    public Boolean getActivityDateClash(CouponActivityAddCondition condition) {
        Integer count = couponActivityMapper.getActivityDateClash(condition);
        if(count>0){
            return true;
        }
        return false;
    }


    /**
     * 自动生成32位的UUid，对应数据库的主键id进行插入用。
     * @return
     */
    public String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
