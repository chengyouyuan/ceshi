package com.winhxd.b2c.common.feign.promotion;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.ServiceName;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.promotion.condition.*;
import com.winhxd.b2c.common.domain.promotion.vo.CouponDiscountVO;
import com.winhxd.b2c.common.domain.promotion.vo.CouponInvestorAmountVO;
import com.winhxd.b2c.common.domain.promotion.vo.CouponVO;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author liuhanning
 * @date  2018年8月6日 下午5:21:22
 * @Description 
 * @version 
 */
@FeignClient(value = ServiceName.PROMOTION_SERVICE, fallbackFactory = CouponServiceFallback.class)
public interface CouponServiceClient {
	
	@RequestMapping(value = "/promotion/5038/v1/getCouponNumsByCustomerForStore", method = RequestMethod.GET)
	ResponseResult<String> getCouponNumsByCustomerForStore(@RequestParam("customerId") Long customerId);

    @RequestMapping(value = "/promotion/5039/v1/orderUseCoupon", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseResult<Boolean> orderUseCoupon(@RequestBody OrderUseCouponCondition condition);

    @RequestMapping(value = "/promotion/5040/v1/orderUntreadCoupon", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseResult<Boolean> orderUntreadCoupon(@RequestBody OrderUntreadCouponCondition condition);

    @RequestMapping(value = "/promotion/5041/v1/revokeCoupon", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseResult<Boolean> revokeCoupon(@RequestBody RevokeCouponCodition condition);

    @RequestMapping(value = "/promotion/5042/v1/couponListByOrder", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseResult<List<CouponVO>> couponListByOrder(@RequestBody OrderCouponCondition couponCondition);

//    @RequestMapping(value = "/promotion/5043/v1/availableCouponListByOrder", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    ResponseResult<List<CouponVO>> availableCouponListByOrder(@RequestBody CouponPreAmountCondition couponCondition);

    @RequestMapping(value = "/promotion/5044/v1/couponDiscountAmount", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseResult<CouponDiscountVO> couponDiscountAmount(@RequestBody CouponPreAmountCondition couponCondition);

    @RequestMapping(value = "/promotion/5007/v1/checkCouponStatus", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseResult<Boolean> checkCouponStatus(@RequestBody CouponCheckStatusCondition condition);

    @RequestMapping(value = "/promotion/5046/v1/getCouponInvestorAmount", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseResult<List<CouponInvestorAmountVO>> getCouponInvestorAmount(@RequestBody CouponInvestorAmountCondition condition);

    @RequestMapping(value = "/promotion/5058/v1/findDefaultCoupon", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseResult<CouponVO> findDefaultCoupon(@RequestBody OrderAvailableCouponCondition condition);

}


@Component
class CouponServiceFallback implements FallbackFactory<CouponServiceClient>{
	private static final Logger logger = LoggerFactory.getLogger(CouponTemplateServiceFallback.class);
    @Override
    public CouponServiceClient create(Throwable throwable) {
        return new CouponServiceClient() {
            @Override
            public ResponseResult<String> getCouponNumsByCustomerForStore(Long customerId) {
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

            @Override
            public ResponseResult couponListByOrder(OrderCouponCondition couponCondition) {
                logger.error("CouponServiceClient -> couponListByOrder", throwable);
                return new ResponseResult<String>(BusinessCode.CODE_1001);
            }

//    @Override
//    public ResponseResult availableCouponListByOrder(CouponPreAmountCondition couponCondition) {
//        logger.error("CouponServiceClient -> availableCouponListByOrder", throwable);
//        return new ResponseResult<String>(BusinessCode.CODE_1001);
//    }

            @Override
            public ResponseResult couponDiscountAmount(CouponPreAmountCondition couponCondition) {
                logger.error("CouponServiceClient -> couponAmountCompute", throwable);
                return new ResponseResult<String>(BusinessCode.CODE_1001);
            }

            @Override
            public ResponseResult checkCouponStatus(CouponCheckStatusCondition condition) {
                logger.error("CouponServiceClient -> checkCouponStatus", throwable);
                return new ResponseResult<String>(BusinessCode.CODE_1001);
            }

            @Override
            public ResponseResult getCouponInvestorAmount(CouponInvestorAmountCondition condition) {
                logger.error("CouponServiceClient -> getCouponInvestorAmount", throwable);
                return new ResponseResult<String>(BusinessCode.CODE_1001);
            }

            @Override
            public ResponseResult findDefaultCoupon(OrderAvailableCouponCondition condition) {
                logger.error("CouponServiceClient -> findDefaultCouponByOrder", throwable);
                return new ResponseResult<String>(BusinessCode.CODE_1001);
            }
        };
    }



}
