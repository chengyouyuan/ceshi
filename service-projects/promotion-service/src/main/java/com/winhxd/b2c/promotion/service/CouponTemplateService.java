package com.winhxd.b2c.promotion.service;

import com.winhxd.b2c.common.domain.promotion.condition.CouponTemplateCondition;

/**
 * @Author wl
 * @Date 2018/8/6 10:51
 * @Description 优惠券模板服务接口
 **/
public interface CouponTemplateService {
    /**
     *
     *@Deccription 添加优惠换模板
     *@Params  [couponTemplateCondition]
     *@Return  ResponseResult
     *@User  wl
     *@Date   2018/8/6 10:45
     */
    int saveCouponTemplate(CouponTemplateCondition couponTemplateCondition);
}
