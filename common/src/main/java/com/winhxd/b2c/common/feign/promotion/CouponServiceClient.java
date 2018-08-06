package com.winhxd.b2c.common.feign.promotion;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

import com.winhxd.b2c.common.constant.ServiceName;

/**
 * @author liuhanning
 * @date  2018年8月6日 下午5:21:22
 * @Description 
 * @version 
 */
@FeignClient(value = ServiceName.PROMOTION_SERVICE, fallbackFactory = CouponServiceFallback.class)
public interface CouponServiceClient {

	
}


@Component
class CouponServiceFallback implements CouponServiceClient{


}
