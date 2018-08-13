package com.winhxd.b2c.promotion.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
import com.winhxd.b2c.promotion.dao.CouponActivityMapper;
import com.winhxd.b2c.promotion.dao.CouponActivityStoreCustomerMapper;
import com.winhxd.b2c.promotion.dao.CouponActivityTemplateMapper;
import com.winhxd.b2c.promotion.service.CouponActivityService;
import com.winhxd.b2c.promotion.service.CouponService;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
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
        String createdStartStr = DateFormatUtils.format(condition.getDateInterval().getStartDate(),"yyyy-MM-dd 00:00:00");
        String createdEndStr = DateFormatUtils.format(condition.getDateInterval().getEndDate(),"yyyy-MM-dd 23:59:59");

        if(condition.getCreatedStart() != null && condition.getCreatedEnd() != null){
            try {
                Date createdStart  = DateUtils.parseDate(createdStartStr);
                Date createdEnd  = DateUtils.parseDate(createdEndStr);
                condition.setCreatedStart(createdStart);
                condition.setCreatedEnd(createdEnd);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        if(condition.getCreatedStart() != null && condition.getCreatedEnd() == null){
            try {
                Date createdStart  = DateUtils.parseDate(createdStartStr);
                condition.setCreatedStart(createdStart);
            }catch (Exception e ){
                e.printStackTrace();
            }
        }
        if(condition.getCreatedStart() == null && condition.getCreatedEnd() != null){
            try {
                Date createdEnd  = DateUtils.parseDate(createdEndStr);
                condition.setCreatedEnd(createdEnd);
            }catch (Exception e ){
                e.printStackTrace();
            }
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
    public void saveCouponActivity(CouponActivityAddCondition condition) {
        try {
            String createdStartStr = DateFormatUtils.format(condition.getDateInterval().getStartDate(),"yyyy-MM-dd 00:00:00");
            String createdEndStr = DateFormatUtils.format(condition.getDateInterval().getEndDate(),"yyyy-MM-dd 23:59:59");
            Date activityStart  = DateUtils.parseDate(createdStartStr);
            Date activityEnd  = DateUtils.parseDate(createdEndStr);
            //CouponActivity
            CouponActivity couponActivity = new CouponActivity();
            couponActivity.setName(condition.getName());
            couponActivity.setCode(condition.getCode());
            couponActivity.setExolian(condition.getExolian());
            couponActivity.setRemarks(condition.getRemarks());
            couponActivity.setActivityStart(activityStart);
            couponActivity.setActivityEnd(activityEnd);
            couponActivity.setActivityStatus(CouponActivityEnum.ACTIVITY_OPEN.getCode());
            couponActivity.setStatus(CouponActivityEnum.ACTIVITY_VALIDATE.getCode());
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
            couponActivityMapper.insertSelective(couponActivity);

            //CouponActivityTemplate
            CouponActivityTemplate couponActivityTemplate = new CouponActivityTemplate();
            for (int i=0 ; i < condition.getCouponActivityTemplateList().size(); i++) {
                couponActivityTemplate.setCouponActivityId(couponActivity.getId());
                couponActivityTemplate.setTemplateId(condition.getCouponActivityTemplateList().get(i).getTemplateId());
                //领券
                if(CouponActivityEnum.PULL_COUPON.getCode() == condition.getType()){
                    String couponStartStr = DateFormatUtils.format(condition.getCouponActivityTemplateList().get(i).getStartTime(),"yyyy-MM-dd 00:00:00");
                    String couponEndStr = DateFormatUtils.format(condition.getCouponActivityTemplateList().get(i).getEndTime(),"yyyy-MM-dd 23:59:59");
                    Date couponStart  = DateUtils.parseDate(couponStartStr);
                    Date couponEnd  = DateUtils.parseDate(couponEndStr);
                    couponActivityTemplate.setStartTime(couponStart);
                    couponActivityTemplate.setEndTime(couponEnd);
                    couponActivityTemplate.setCouponNumType(condition.getCouponActivityTemplateList().get(i).getCouponNumType());
                    couponActivityTemplate.setCouponNum(condition.getCouponActivityTemplateList().get(i).getCouponNum());
                    couponActivityTemplate.setCustomerVoucherLimitType(condition.getCouponActivityTemplateList().get(i).getCustomerVoucherLimitType());
                    couponActivityTemplate.setStatus(CouponActivityEnum.ACTIVITY_EFFICTIVE.getCode());
                    if(condition.getCouponActivityTemplateList().get(i).getCustomerVoucherLimitType() == CouponActivityEnum.STORE_LIMITED.getCode()){
                        couponActivityTemplate.setCustomerVoucherLimitNum(condition.getCouponActivityTemplateList().get(i).getCustomerVoucherLimitNum());
                    }
                }
                //推券
                if(CouponActivityEnum.PUSH_COUPON.getCode() == condition.getType()){
                    couponActivityTemplate.setEffectiveDays(condition.getCouponActivityTemplateList().get(i).getEffectiveDays());
                    couponActivityTemplate.setCustomerVoucherLimitNum(condition.getCouponActivityTemplateList().get(i).getCustomerVoucherLimitNum());
                }
                couponActivityTemplateMapper.insertSelective(couponActivityTemplate);

                //coupon_activity_store_customer
                CouponActivityStoreCustomer couponActivityStoreCustomer  = new CouponActivityStoreCustomer();
                for (int j=0 ; j < condition.getCouponActivityTemplateList().get(i).getCouponActivityStoreCustomerList().size(); j++) {
                    couponActivityStoreCustomer.setCouponActivityTemplateId(couponActivityTemplate.getId());
                    couponActivityStoreCustomer.setStoreId(condition.getCouponActivityTemplateList().get(i).getCouponActivityStoreCustomerList().get(j).getStoreId());
                    couponActivityStoreCustomer.setCustomerId(condition.getCouponActivityTemplateList().get(i).getCouponActivityStoreCustomerList().get(j).getCustomerId());
                    couponActivityStoreCustomer.setStatus(CouponActivityEnum.ACTIVITY_EFFICTIVE.getCode());
                    couponActivityStoreCustomerMapper.insertSelective(couponActivityStoreCustomer);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
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
        List<CouponActivityTemplate> couponActivityTemplateList = couponActivityTemplateMapper.selectByActivityId(couponActivity.getId());
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
    public void updateCouponActivity(CouponActivityAddCondition condition) {
        try {

            String activityStartStr = DateFormatUtils.format(condition.getActivityStart(),"yyyy-MM-dd 00:00:00");
            String activityEndStr = DateFormatUtils.format(condition.getActivityEnd(),"yyyy-MM-dd 23:59:59");
            Date activityStart  = DateUtils.parseDate(activityStartStr);
            Date activityEnd  = DateUtils.parseDate(activityEndStr);
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
            couponActivity.setStatus(CouponActivityEnum.ACTIVITY_VALIDATE.getCode());
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
            couponActivityMapper.updateByPrimaryKeySelective(couponActivity);

            //删除CouponActivityTemplate
            CouponActivityTemplate couponActivityTemplate = new CouponActivityTemplate();
            couponActivityTemplate.setCouponActivityId(condition.getId());
            couponActivityTemplate.setStatus(CouponActivityEnum.ACTIVITY_VALIDATE.getCode());
            couponActivityTemplateMapper.updateByCouponActivityId(couponActivityTemplate);
            //删除couponActivityStoreCustomer
            CouponActivityStoreCustomer couponActivityStoreCustomer = new CouponActivityStoreCustomer();
            couponActivityStoreCustomer.setCouponActivityTemplateId(condition.getCouponActivityTemplateList().get(0).getId());
            couponActivityStoreCustomer.setStatus(CouponActivityEnum.ACTIVITY_VALIDATE.getCode());
            couponActivityStoreCustomerMapper.updateByCouponActivityTemplateId(couponActivityStoreCustomer);

            //新增couponActivityTemplate
            for (int i=0 ; i < condition.getCouponActivityTemplateList().size(); i++) {
                couponActivityTemplate.setCouponActivityId(couponActivity.getId());
                couponActivityTemplate.setTemplateId(condition.getCouponActivityTemplateList().get(i).getTemplateId());
                //领券
                if(CouponActivityEnum.PULL_COUPON.getCode() == condition.getType()){
                    String couponStartStr = DateFormatUtils.format(condition.getCouponActivityTemplateList().get(i).getStartTime(),"yyyy-MM-dd 00:00:00");
                    String couponEndStr = DateFormatUtils.format(condition.getCouponActivityTemplateList().get(i).getEndTime(),"yyyy-MM-dd 23:59:59");
                    Date couponStart  = DateUtils.parseDate(couponStartStr);
                    Date couponEnd  = DateUtils.parseDate(couponEndStr);
                    couponActivityTemplate.setStartTime(couponStart);
                    couponActivityTemplate.setEndTime(couponEnd);
                    couponActivityTemplate.setCouponNumType(condition.getCouponActivityTemplateList().get(i).getCouponNumType());
                    couponActivityTemplate.setCouponNum(condition.getCouponActivityTemplateList().get(i).getCouponNum());
                    couponActivityTemplate.setCustomerVoucherLimitType(condition.getCouponActivityTemplateList().get(i).getCustomerVoucherLimitType());
                    couponActivityTemplate.setStatus(CouponActivityEnum.ACTIVITY_EFFICTIVE.getCode());
                    if(condition.getCouponActivityTemplateList().get(i).getCustomerVoucherLimitType() == CouponActivityEnum.STORE_LIMITED.getCode()){
                        couponActivityTemplate.setCustomerVoucherLimitNum(condition.getCouponActivityTemplateList().get(i).getCustomerVoucherLimitNum());
                    }
                }
                //推券
                if(CouponActivityEnum.PUSH_COUPON.getCode() == condition.getType()){
                    couponActivityTemplate.setEffectiveDays(condition.getCouponActivityTemplateList().get(i).getEffectiveDays());
                    couponActivityTemplate.setCustomerVoucherLimitNum(condition.getCouponActivityTemplateList().get(i).getCustomerVoucherLimitNum());
                }
                couponActivityTemplateMapper.insertSelective(couponActivityTemplate);

                //新增couponActivityStoreCustomer
                for (int j=0 ; j < condition.getCouponActivityTemplateList().get(i).getCouponActivityStoreCustomerList().size(); j++) {
                    couponActivityStoreCustomer.setCouponActivityTemplateId(couponActivityTemplate.getId());
                    couponActivityStoreCustomer.setStoreId(condition.getCouponActivityTemplateList().get(i).getCouponActivityStoreCustomerList().get(j).getStoreId());
                    couponActivityStoreCustomer.setCustomerId(condition.getCouponActivityTemplateList().get(i).getCouponActivityStoreCustomerList().get(j).getCustomerId());
                    couponActivityStoreCustomer.setStatus(CouponActivityEnum.ACTIVITY_EFFICTIVE.getCode());
                    couponActivityStoreCustomerMapper.insertSelective(couponActivityStoreCustomer);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * 删除活动信息（更新活动状态为无效）
     * @param condition
     */
    @Override
    public void deleteCouponActivity(CouponActivityCondition condition) {
        CouponActivity couponActivity = new CouponActivity();
        couponActivity.setId(condition.getId());
        couponActivity.setStatus(CouponActivityEnum.ACTIVITY_VALIDATE.getCode());
        couponActivity.setUpdated(new Date());
        couponActivity.setUpdatedBy(condition.getCreatedBy());
        couponActivity.setUpdatedByName(condition.getCreatedByName());
        couponActivityMapper.updateByPrimaryKeySelective(couponActivity);
    }

    /**
     * 停止活动，撤销优惠券
     * @param condition
     */
    @Override
    public void revocationActivityCoupon(CouponActivityCondition condition) {
        //停止活动
        CouponActivity couponActivity = new CouponActivity();
        couponActivity.setId(condition.getId());
        couponActivity.setActivityStatus(CouponActivityEnum.ACTIVITY_STOP.getCode());
        couponActivity.setUpdated(new Date());
        couponActivity.setUpdatedBy(condition.getCreatedBy());
        couponActivity.setUpdatedByName(condition.getCreatedByName());

        couponActivityMapper.updateByPrimaryKeySelective(couponActivity);
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
    public void updateCouponActivityStatus(CouponActivityAddCondition condition) {
        //更新CouponActivity
        CouponActivity couponActivity = new CouponActivity();
        couponActivity.setId(condition.getId());
        couponActivity.setActivityStatus(condition.getActivityStatus());
        couponActivity.setUpdated(new Date());
        couponActivity.setUpdatedBy(condition.getCreatedBy());
        couponActivity.setUpdatedByName(condition.getCreatedByName());

        couponActivityMapper.updateByPrimaryKeySelective(couponActivity);
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
