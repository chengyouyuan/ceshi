package com.winhxd.b2c.common.feign.promotion;

import com.winhxd.b2c.common.domain.promotion.condition.CouponCondition;
import com.winhxd.b2c.common.domain.promotion.condition.OrderUntreadCouponCondition;
import com.winhxd.b2c.common.domain.promotion.condition.OrderUseCouponCondition;
import com.winhxd.b2c.common.domain.promotion.condition.RevokeCouponCodition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
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
	
	@RequestMapping(value = "/coupon/getCouponNumsByCustomerForStore", method = RequestMethod.GET)
	ResponseResult<String> getCouponNumsByCustomerForStore(@RequestParam("storeId") Long storeId,@RequestParam("customerId") Long customerId);

    @RequestMapping(value = "/coupon/orderUseCoupon", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseResult orderUseCoupon(@RequestBody OrderUseCouponCondition condition);

    @RequestMapping(value = "/coupon/orderUntreadCoupon", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseResult orderUntreadCoupon(@RequestBody OrderUntreadCouponCondition condition);

    @RequestMapping(value = "/coupon/revokeCoupon", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseResult revokeCoupon(@RequestBody RevokeCouponCodition condition);
	
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
	public ResponseResult<String> getCouponNumsByCustomerForStore(Long storeId,Long customerId) {
		 logger.error("CouponServiceClient -> getCouponNumsByCustomerForStore", throwable);
	     return new ResponseResult<String>(BusinessCode.CODE_1001);
	}

    @Override
    public ResponseResult orderUseCoupon(OrderUseCouponCondition condition) {
        logger.error("CouponServiceClient -> orderUseCoupon", throwable);
        return new ResponseResult<String>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult orderUntreadCoupon(OrderUntreadCouponCondition condition) {
        logger.error("CouponServiceClient -> orderUntreadCoupon", throwable);
        return new ResponseResult<String>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult revokeCoupon(RevokeCouponCodition condition) {
        logger.error("CouponServiceClient -> revokeCoupon", throwable);
        return new ResponseResult<String>(BusinessCode.CODE_1001);
    }


}
