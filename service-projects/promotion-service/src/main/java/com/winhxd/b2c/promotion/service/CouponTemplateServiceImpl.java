package com.winhxd.b2c.promotion.service;

import com.winhxd.b2c.common.domain.promotion.condition.CouponTemplateCondition;
import com.winhxd.b2c.common.domain.promotion.enums.CouponTemplateEnum;
import com.winhxd.b2c.common.domain.promotion.model.CouponTemplate;
import com.winhxd.b2c.promotion.dao.CouponTemplateMapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author wl
 * @Date 2018/8/6 10:52
 * @Description 优惠券模板服务接口 实现类
 **/
public class CouponTemplateServiceImpl implements CouponTemplateService{
     @Autowired
     private CouponTemplateMapper couponTemplateMapper;
    /**
     *
     *@Deccription 添加优惠换模板
     *@Params  [couponTemplateCondition]
     *@Return  ResponseResult
     *@User  wl
     *@Date   2018/8/6 10:45
     */
    @Override
    public int saveCouponTemplate(CouponTemplateCondition couponTemplateCondition) {
        CouponTemplate couponTemplate = new CouponTemplate();
        couponTemplate.setTitle(couponTemplateCondition.getTitle());
        couponTemplate.setExolian(couponTemplateCondition.getExolian());
        couponTemplate.setRemarks(couponTemplateCondition.getRemarks());
        couponTemplate.setInvestorId(couponTemplateCondition.getInvestorId());
        couponTemplate.setUseRangeId(couponTemplateCondition.getUseRangeId());
        couponTemplate.setApplyRuleId(couponTemplateCondition.getApplyRuleId());
        couponTemplate.setCouponLabel(couponTemplateCondition.getCouponLabel());
        couponTemplate.setStatus(CouponTemplateEnum.EFFICTIVE.getCode());
        couponTemplate.setCalType(couponTemplateCondition.getCalType());
        couponTemplate.setPayType(couponTemplateCondition.getPayType());
//        couponTemplate.setCreated();
//        couponTemplate.setCreatedByName();
//        couponTemplate.setCreatedBy();
        return couponTemplateMapper.insert(couponTemplate);
    }
}
