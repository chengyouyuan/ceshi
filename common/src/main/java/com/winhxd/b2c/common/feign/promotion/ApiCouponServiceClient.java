package com.winhxd.b2c.common.feign.promotion;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.ServiceName;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.promotion.condition.CouponCondition;
import com.winhxd.b2c.common.domain.promotion.vo.CouponVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @Auther wangxiaoshun
 * @Date 2018/8/6 15 20
 * @Description
 */
@FeignClient(value = ServiceName.PROMOTION_SERVICE, fallbackFactory = ApiCouponServiceFallback.class)
public interface ApiCouponServiceClient {

    /**
     * 新人专享优惠列表
     * @param couponCondition
     * @return
     */
    @RequestMapping(value = "/501/v1/getNewUserCouponList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseResult<List<CouponVO>> getNewUserCouponList(@RequestBody CouponCondition couponCondition);

    /**
     * 待领取优惠券列表
     * @param couponCondition
     * @return
     */
    @RequestMapping(value = "/502/v1/unclaimedCouponList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseResult<PagedList<CouponVO>> unclaimedCouponList(@RequestBody CouponCondition couponCondition);

    /**
     * 我的优惠券列表
     * @param couponCondition
     * @return
     */
    @RequestMapping(value = "/503/v1/myCouponList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseResult<PagedList<CouponVO>> myCouponList(@RequestBody CouponCondition couponCondition);

    /**
     * 用户领取优惠券
     * @param couponCondition
     * @return
     */
    @RequestMapping(value = "/504/v1/userReceiveCoupon", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseResult<Boolean> userReceiveCoupon(@RequestBody CouponCondition couponCondition);

    /**
     * 根据订单查询优惠券列表
     * @param couponCondition
     * @return
     */
    @RequestMapping(value = "/505/v1/couponListByOrder", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseResult<PagedList<CouponVO>> couponListByOrder(@RequestBody CouponCondition couponCondition);

}

@Component
class ApiCouponServiceFallback implements ApiCouponServiceClient{
    private static final Logger logger = LoggerFactory.getLogger(ApiCouponServiceClient.class);
    private Throwable throwable;

    /**
     * 新人专享优惠列表
     * @param couponCondition
     * @return
     */
    @Override
    public ResponseResult getNewUserCouponList(CouponCondition couponCondition) {
        logger.error("ApiCouponServiceClient -> getNewUserCouponList", throwable);
        return new ResponseResult<Integer>(BusinessCode.CODE_1001);
    }

    /**
     * 待领取优惠券列表
     * @param couponCondition
     * @return
     */
    @Override
    public ResponseResult unclaimedCouponList(CouponCondition couponCondition) {
        logger.error("ApiCouponServiceClient -> unclaimedCouponList", throwable);
        return new ResponseResult<Integer>(BusinessCode.CODE_1001);
    }

    /**
     * 我的优惠券列表
     * @param couponCondition
     * @return
     */
    @Override
    public ResponseResult myCouponList(CouponCondition couponCondition) {
        logger.error("ApiCouponServiceClient -> myCouponList", throwable);
        return new ResponseResult<Integer>(BusinessCode.CODE_1001);
    }

    /**
     * 用户领取优惠券
     * @param couponCondition
     * @return
     */
    @Override
    public ResponseResult userReceiveCoupon(CouponCondition couponCondition) {
        logger.error("ApiCouponServiceClient -> userReceiveCoupon", throwable);
        return new ResponseResult<Integer>(BusinessCode.CODE_1001);
    }

    /**
     * 根据订单查询优惠券列表
     * @param couponCondition
     * @return
     */
    @Override
    public ResponseResult couponListByOrder(CouponCondition couponCondition) {
        logger.error("ApiCouponServiceClient -> couponListByOrder", throwable);
        return new ResponseResult<Integer>(BusinessCode.CODE_1001);
    }

}
