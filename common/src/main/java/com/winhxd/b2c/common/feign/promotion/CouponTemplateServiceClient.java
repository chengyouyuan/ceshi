package com.winhxd.b2c.common.feign.promotion;

import com.winhxd.b2c.common.constant.ServiceName;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Author wl
 * @Date 2018/8/6 09:51
 * @Description  优惠券模板接口
 **/
@FeignClient(value = ServiceName.COUPON_SERVICE, fallbackFactory = CouponInvestorServiceFallback.class)
public interface CouponTemplateServiceClient {
@RequestMapping(value = "/promotion/v1/addCouponTemplate", method = RequestMethod.POST)
public int addCouponTemplate();

}
