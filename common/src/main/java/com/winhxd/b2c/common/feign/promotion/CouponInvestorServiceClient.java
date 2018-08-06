package com.winhxd.b2c.common.feign.promotion;

import com.winhxd.b2c.common.constant.ServiceName;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Author wl
 * @Date 2018/8/6 09:35
 * @Description  出资方管理相关接口
 **/

@FeignClient(value = ServiceName.PROMOTION_SERVICE, fallbackFactory = CouponInvestorServiceFallback.class)
public interface CouponInvestorServiceClient {
    @RequestMapping(value = "/promotion/v1/addCouponInvestor/", method = RequestMethod.GET)
    public int addCouponInvestor();



}

@Component
class CouponInvestorServiceFallback implements CouponInvestorServiceClient{


    @Override
    public int addCouponInvestor() {
        return 0;
    }
}
