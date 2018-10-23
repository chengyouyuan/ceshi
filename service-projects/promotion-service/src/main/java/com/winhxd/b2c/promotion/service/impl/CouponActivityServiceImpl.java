package com.winhxd.b2c.promotion.service.impl;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.context.CustomerUser;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.promotion.condition.CouponActivityAddCondition;
import com.winhxd.b2c.common.domain.promotion.condition.CouponActivityCondition;
import com.winhxd.b2c.common.domain.promotion.condition.RevokeCouponCodition;
import com.winhxd.b2c.common.domain.promotion.enums.CouponActivityEnum;
import com.winhxd.b2c.common.domain.promotion.model.*;
import com.winhxd.b2c.common.domain.promotion.vo.CouponActivityStoreVO;
import com.winhxd.b2c.common.domain.promotion.vo.CouponActivityVO;
import com.winhxd.b2c.common.domain.store.condition.StoreCustomerRegionCondition;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.store.StoreServiceClient;
import com.winhxd.b2c.common.util.DateUtil;
import com.winhxd.b2c.promotion.dao.*;
import com.winhxd.b2c.promotion.service.CouponActivityService;
import com.winhxd.b2c.promotion.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

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
    @Autowired
    private CouponPushCustomerMapper couponPushCustomerMapper;

    @Autowired
    private StoreServiceClient storeServiceClient;

    /**
     * 查询活动列表
     * @param condition
     */
    @Override
    public List<CouponActivityVO> findCouponActivity(CouponActivityCondition condition) {
        if(condition.getDateInterval() != null){
            if(condition.getDateInterval().getStart() != null){
                condition.setCreatedStart(DateUtil.getStartDate(condition.getDateInterval().getStart()));
            }
        }
        if (condition.getDateInterval().getEnd() != null) {
            condition.setCreatedEnd(DateUtil.getEndDate(condition.getDateInterval().getEnd()));
        }

        List<CouponActivityVO> activity = couponActivityMapper.selectCouponActivity(condition);
        if (!CollectionUtils.isEmpty(activity)) {

            List<Long> activityIds = activity.stream().map(ac -> ac.getId()).collect(Collectors.toList());
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
		if (!CollectionUtils.isEmpty(list)) {
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

        CouponActivity couponActivity = new CouponActivity();

        couponActivity.setName(condition.getName());
        couponActivity.setCode(condition.getCode());
        couponActivity.setExolian(condition.getExolian());
        couponActivity.setRemarks(condition.getRemarks());
        couponActivity.setActivityStart(DateUtil.getStartDate(condition.getActivityStart()));
        couponActivity.setActivityEnd(DateUtil.getEndDate(condition.getActivityEnd()));
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
            couponActivity.setCouponType(condition.getCouponType());//注意前端 传过来的值是否有效，以前都是默认写死成新用户
        }
        int n = couponActivityMapper.insertSelective(couponActivity);
        if(n==0){
            throw new BusinessException(BusinessCode.CODE_503001,"优惠券活动添加失败");
        }
        //获取区域信息
        if(!CollectionUtils.isEmpty(condition.getCouponActivityAreaList())){
            for (CouponActivityArea couponActivityArea:condition.getCouponActivityAreaList()){
                couponActivityArea.setCouponActivityId(couponActivity.getId());
                couponActivityArea.setStatus(CouponActivityEnum.ACTIVITY_EFFICTIVE.getCode());
                int n4 = couponActivityAreaMapper.insertSelective(couponActivityArea);
                if(n4==0){
                    throw new BusinessException(BusinessCode.CODE_503001,"优惠券活动添加失败");
                }
            }
        }
        List<CustomerUser> couponActivityCustomerList = condition.getCouponActivityCustomerList();

        //CouponActivityTemplate
        for (CouponActivityTemplate cat:condition.getCouponActivityTemplateList()) {
            cat.setCouponActivityId(couponActivity.getId());
            cat.setStatus(CouponActivityEnum.ACTIVITY_EFFICTIVE.getCode());
            //领券
            if(CouponActivityEnum.PULL_COUPON.getCode() == condition.getType()){
                cat.setStartTime(DateUtil.getStartDate(cat.getStartTime()));
                cat.setEndTime(DateUtil.getEndDate(cat.getEndTime()));
                if(cat.getCustomerVoucherLimitType() != CouponActivityEnum.STORE_LIMITED.getCode()){
                    cat.setCustomerVoucherLimitNum(null);
                }
                //如果不限制则最大张数限定100张
                if(cat.getCustomerVoucherLimitType() == CouponActivityEnum.UNLIMITED.getCode()){
                    cat.setCustomerVoucherLimitNum(100);
                }
            }
            //推券
            if(CouponActivityEnum.PUSH_COUPON.getCode() == condition.getType()){
                Integer effectiveDays = cat.getEffectiveDays();
                if( effectiveDays == null || effectiveDays<=0){
                    cat.setStartTime(DateUtil.getStartDate(cat.getStartTime()));
                    cat.setEndTime(DateUtil.getEndDate(cat.getEndTime()));
                }
                cat.setCouponNumType((short)1);
            }
            int n2 = couponActivityTemplateMapper.insertSelective(cat);
            if(n2==0){
                throw new BusinessException(BusinessCode.CODE_503001,"优惠券活动添加失败");
            }

            //推券领券区域划分 coupon_activity_store_customer
            if(CouponActivityEnum.PULL_COUPON.getCode() == condition.getType()
                    || (CouponActivityEnum.PUSH_COUPON.getCode() == condition.getType()
                        && condition.getCouponType() == CouponActivityEnum.OLD_USER.getCode()
                        && CollectionUtils.isEmpty(couponActivityCustomerList))){

                if(!CollectionUtils.isEmpty(cat.getCouponActivityStoreCustomerList())){
                    for (CouponActivityStoreCustomer casc:cat.getCouponActivityStoreCustomerList()) {
                        casc.setCouponActivityTemplateId(cat.getId());
                        casc.setStatus(CouponActivityEnum.ACTIVITY_EFFICTIVE.getCode());
                        int n3 = couponActivityStoreCustomerMapper.insertSelective(casc);
                        if(n3==0){
                            throw new BusinessException(BusinessCode.CODE_503001,"优惠券活动添加失败");
                        }
                    }
                }
            }
        }
        //指定具体人进行推券 coupon_push_customer
        if(CouponActivityEnum.PUSH_COUPON.getCode() == condition.getType()
                && condition.getCouponType() == CouponActivityEnum.OLD_USER.getCode()
                && !CollectionUtils.isEmpty(couponActivityCustomerList)){

            for(CustomerUser customerUser:couponActivityCustomerList){
                CouponPushCustomer couponPushCustomer = new CouponPushCustomer();
                couponPushCustomer.setCouponActivityId(couponActivity.getId());
                couponPushCustomer.setCustomerId(customerUser.getCustomerId());
                couponPushCustomer.setReceive(false);
                int effLine = couponPushCustomerMapper.insert(couponPushCustomer);
                if(effLine==0){
                    throw new BusinessException(BusinessCode.CODE_503001,"优惠券活动添加失败");
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
        List<CouponActivityStoreCustomer> couponActivityStoreCustomerList = null;

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

        if(couponActivity.getType() == CouponActivityEnum.PUSH_COUPON.getCode()){
            couponActivityVO.setCouponType(couponActivity.getCouponType());
            if(couponActivity.getCouponType() == CouponActivityEnum.OLD_USER.getCode()){//老用户
                List<CouponPushCustomer> couponPushCustomers = couponPushCustomerMapper.getCouponPushCustomerByActiveId(Long.valueOf(id));
                couponActivityVO.setCouponPushCustomerList(couponPushCustomers);

                List<String> list = new ArrayList<>();
                if(!CollectionUtils.isEmpty(couponActivityStoreCustomerList)){
                    for(CouponActivityStoreCustomer casc :couponActivityStoreCustomerList){
                        list.add(String.valueOf(casc.getStoreId()));
                    }
                }
                if(!CollectionUtils.isEmpty(list)){
                    StoreCustomerRegionCondition scrc = new StoreCustomerRegionCondition();
                    scrc.setStoreUserInfoIds(list);
                    ResponseResult<List<Long>> storeCustomerRegions = storeServiceClient.findStoreCustomerRegions(scrc);
                    couponActivityVO.setStoreCustomerNum(storeCustomerRegions.getData() == null ?0:(long)storeCustomerRegions.getData().size());
                }
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
     
        Date activityStart = DateUtil.getStartDate(condition.getActivityStart());
        Date activityEnd = DateUtil.getEndDate(condition.getActivityEnd());

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
                
                Date couponStart = DateUtil.getStartDate(condition.getCouponActivityTemplateList().get(i).getStartTime());
                Date couponEnd = DateUtil.getEndDate(condition.getCouponActivityTemplateList().get(i).getEndTime());
                
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
        couponActivity.setActivityStatus(CouponActivityEnum.ACTIVITY_STOP_UNDO.getCode());
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
