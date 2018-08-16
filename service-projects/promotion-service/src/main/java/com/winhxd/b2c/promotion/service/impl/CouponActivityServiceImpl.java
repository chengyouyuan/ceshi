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
import com.winhxd.b2c.common.domain.promotion.model.CouponActivity;
import com.winhxd.b2c.common.domain.promotion.model.CouponActivityStoreCustomer;
import com.winhxd.b2c.common.domain.promotion.model.CouponActivityTemplate;
import com.winhxd.b2c.common.domain.promotion.vo.CouponActivityStoreVO;
import com.winhxd.b2c.common.domain.promotion.vo.CouponActivityVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.promotion.dao.CouponActivityMapper;
import com.winhxd.b2c.promotion.dao.CouponActivityStoreCustomerMapper;
import com.winhxd.b2c.promotion.dao.CouponActivityTemplateMapper;
import com.winhxd.b2c.promotion.service.CouponActivityService;
import com.winhxd.b2c.promotion.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
    @Autowired
    private CouponActivityTemplateMapper couponActivityTemplateMapper;
    @Autowired
    private CouponActivityStoreCustomerMapper couponActivityStoreCustomerMapper;
    @Autowired
    private CouponService couponService;

    /**
     * 查询活动列表
     * @param condition
     */
    @Override
    public ResponseResult<PagedList<CouponActivityVO>> findCouponActivity(CouponActivityCondition condition) {
        if(condition.getDateInterval().getStartDate() != null){
            Calendar createdS = Calendar.getInstance();
            createdS.setTime(condition.getDateInterval().getStartDate());
            createdS.set(Calendar.HOUR_OF_DAY, 0);
            createdS.set(Calendar.MINUTE, 0);
            createdS.set(Calendar.SECOND, 0);
            createdS.set(Calendar.MILLISECOND, 0);
            Date createdStart = createdS.getTime();
            condition.setCreatedStart(createdStart);
        }
        if(condition.getDateInterval().getEndDate() != null){
            Calendar createdE = Calendar.getInstance();
            createdE.setTime(condition.getDateInterval().getEndDate());
            createdE.set(Calendar.HOUR_OF_DAY, 23);
            createdE.set(Calendar.MINUTE, 59);
            createdE.set(Calendar.SECOND, 59);
            createdE.set(Calendar.MILLISECOND, 59);
            Date createdEnd  =createdE.getTime();
            condition.setCreatedEnd(createdEnd);

        }

        ResponseResult<PagedList<CouponActivityVO>> result = new ResponseResult<PagedList<CouponActivityVO>>();
        PagedList<CouponActivityVO> pagedList = new PagedList<>();
        PageHelper.startPage(condition.getPageNo(),condition.getPageSize());
        List<CouponActivityVO> activity = couponActivityMapper.selectCouponActivity(condition);
        PageInfo<CouponActivityVO> pageInfo = new PageInfo<>(activity);
        pagedList.setData(pageInfo.getList());
        pagedList.setPageNo(pageInfo.getPageNum());
        pagedList.setPageSize(pageInfo.getPageSize());
        pagedList.setTotalRows(pageInfo.getTotal());
        result.setData(pagedList);
        return result;
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
        List<CouponActivityTemplate> couponActivityTemplateList = couponActivityTemplateMapper.selectTemplateByActivityId(couponActivity.getId());
        couponActivityVO.setCouponActivityTemplateList(couponActivityTemplateList);

        for(int i = 0 ; i < couponActivityTemplateList.size() ; i++) {
            couponActivityStoreCustomerList = couponActivityStoreCustomerMapper.selectByTemplateId(couponActivityTemplateList.get(i).getId());
            couponActivityVO.getCouponActivityTemplateList().get(i).setCouponActivityStoreCustomerList(couponActivityStoreCustomerList);
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
        //撤销已发放的优惠券
        List<Long> longList = null;
        longList.add(condition.getId());
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
    public ResponseResult<PagedList<CouponActivityStoreVO>> findCouponByActivity(CouponActivityCondition condition) {
        ResponseResult<PagedList<CouponActivityStoreVO>> result = new ResponseResult<PagedList<CouponActivityStoreVO>>();
        PagedList<CouponActivityStoreVO> pagedList = new PagedList<>();
        PageHelper.startPage(condition.getPageNo(),condition.getPageSize());
        List<CouponActivityStoreVO> coupon = couponActivityMapper.selectCouponByActivity(condition);
        PageInfo<CouponActivityStoreVO> pageInfo = new PageInfo<>(coupon);
        pagedList.setData(pageInfo.getList());
        pagedList.setPageNo(pageInfo.getPageNum());
        pagedList.setPageSize(pageInfo.getPageSize());
        pagedList.setTotalRows(pageInfo.getTotal());
        result.setData(pagedList);
        return result;
    }

    /**
     * 根据活动查询小店信息
     * @param condition
     */
    @Override
    public ResponseResult<PagedList<CouponActivityStoreVO>> findStoreByActivity(CouponActivityCondition condition) {
        ResponseResult<PagedList<CouponActivityStoreVO>> result = new ResponseResult<PagedList<CouponActivityStoreVO>>();
        PagedList<CouponActivityStoreVO> pagedList = new PagedList<>();
        PageHelper.startPage(condition.getPageNo(),condition.getPageSize());
        List<CouponActivityStoreVO> store = couponActivityMapper.selectStoreByActivity(condition);
        PageInfo<CouponActivityStoreVO> pageInfo = new PageInfo<>(store);
        pagedList.setData(pageInfo.getList());
        pagedList.setPageNo(pageInfo.getPageNum());
        pagedList.setPageSize(pageInfo.getPageSize());
        pagedList.setTotalRows(pageInfo.getTotal());
        result.setData(pagedList);
        return result;
    }


    /**
     * 自动生成32位的UUid，对应数据库的主键id进行插入用。
     * @return
     */
    public String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
