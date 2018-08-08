package com.winhxd.b2c.promotion.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.promotion.condition.CouponActivityAddCondition;
import com.winhxd.b2c.common.domain.promotion.condition.CouponActivityCondition;
import com.winhxd.b2c.common.domain.promotion.enums.CouponActivityEnum;
import com.winhxd.b2c.common.domain.promotion.model.CouponActivity;
import com.winhxd.b2c.common.domain.promotion.model.CouponActivityStoreCustomer;
import com.winhxd.b2c.common.domain.promotion.model.CouponActivityTemplate;
import com.winhxd.b2c.common.domain.promotion.vo.CouponActivityVO;
import com.winhxd.b2c.common.domain.promotion.vo.CouponTemplateVO;
import com.winhxd.b2c.promotion.dao.CouponActivityMapper;
import com.winhxd.b2c.promotion.dao.CouponActivityStoreCustomerMapper;
import com.winhxd.b2c.promotion.dao.CouponActivityTemplateMapper;
import com.winhxd.b2c.promotion.service.CouponActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public ResponseResult<PagedList<CouponActivityVO>> queryCouponActivity(CouponActivityCondition condition) {
        ResponseResult<PagedList<CouponActivityVO>> result = new ResponseResult<PagedList<CouponActivityVO>>();
        PagedList<CouponActivityVO> pagedList = new PagedList<>();
        PageHelper.startPage(condition.getPageNo(),condition.getPageSize());
        List<CouponActivityVO> activity = couponActivityMapper.queryCouponActivity(condition);
        PageInfo<CouponActivityVO> pageInfo = new PageInfo<>(activity);
        pagedList.setData(pageInfo.getList());
        pagedList.setPageNo(pageInfo.getPageNum());
        pagedList.setPageSize(pageInfo.getPageSize());
        pagedList.setTotalRows(pageInfo.getTotal());
        result.setData(pagedList);
        return result;
    }

    @Override
    public void saveCouponActivity(CouponActivityAddCondition condition) {
        try {
            //CouponActivity
            CouponActivity couponActivity = new CouponActivity();
            couponActivity.setName(condition.getName());
            couponActivity.setCode(getUUID());
            couponActivity.setExolian(condition.getExolian());
            couponActivity.setRemarks(condition.getRemarks());
            couponActivity.setActivityStart(condition.getActivityStart());
            couponActivity.setActivityEnd(condition.getActivityEnd());
            couponActivity.setActivityStatus(CouponActivityEnum.ACTIVITY_OPEN.getCode());
            couponActivity.setStatus(CouponActivityEnum.ACTIVITY_VALIDATE.getCode());
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
                //领券
                if(CouponActivityEnum.PULL_COUPON.getCode() == condition.getType()){
                    couponActivityTemplate.setStartTime(condition.getCouponActivityTemplateList().get(i).getStartTime());
                    couponActivityTemplate.setEndTime(condition.getCouponActivityTemplateList().get(i).getEndTime());
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
                    couponActivityTemplate.setCustomerVoucherLimitNum(condition.getCouponActivityTemplateList().get(i).getCustomerVoucherLimitNum());
                }
                couponActivityTemplateMapper.insertSelective(couponActivityTemplate);

                //coupon_activity_store_customer
                CouponActivityStoreCustomer couponActivityStoreCustomer  = new CouponActivityStoreCustomer();
                for (int j=0 ; j < condition.getCouponActivityTemplateList().get(i).getCouponActivityStoreCustomerList().size(); j++) {
                    couponActivityStoreCustomer.setCouponActivityTemplateId(couponActivityTemplate.getId());
                    couponActivityStoreCustomer.setStoreId(condition.getCouponActivityTemplateList().get(i).getCouponActivityStoreCustomerList().get(j).getStoreId());
                    couponActivityStoreCustomer.setCustomerId(condition.getCouponActivityTemplateList().get(i).getCouponActivityStoreCustomerList().get(j).getCustomerId());
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

    @Override
    public void updateCouponActivity(CouponActivityAddCondition condition) {
    }

    /**
     * 自动生成32位的UUid，对应数据库的主键id进行插入用。
     * @return
     */
    public String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
