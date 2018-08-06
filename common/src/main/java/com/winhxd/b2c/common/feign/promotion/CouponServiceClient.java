package com.winhxd.b2c.common.feign.promotion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.ServiceName;
import com.winhxd.b2c.common.domain.ResponseResult;

import feign.hystrix.FallbackFactory;

/**
 * @author liuhanning
 * @date  2018年8月6日 下午5:21:22
 * @Description 
 * @version 
 */
@FeignClient(value = ServiceName.PROMOTION_SERVICE, fallbackFactory = CouponServiceFallback.class)
public interface CouponServiceClient {
	
	@RequestMapping(value = "/coupon/getCouponNumsByStore", method = RequestMethod.GET)
	ResponseResult<String> getCouponNumsByStore(@RequestParam("storeId") Long storeId);
	
	
	
	
	
	
}


@Component
class CouponServiceFallback implements CouponServiceClient, FallbackFactory<CouponServiceClient>{
	private static final Logger logger = LoggerFactory.getLogger(CouponTemplateServiceFallback.class);
    private Throwable throwable;

    public CouponServiceFallback() {
    }

    private CouponServiceFallback(Throwable throwable) {
        this.throwable = throwable;
    }
    @Override
    public CouponServiceClient create(Throwable throwable) {
        return new CouponServiceFallback(throwable);
    }
	@Override
	public ResponseResult<String> getCouponNumsByStore(Long storeId) {
		 logger.error("CouponServiceClient -> getCouponNumsByStore", throwable);
	     return new ResponseResult<String>(BusinessCode.CODE_1001);
	}



}
