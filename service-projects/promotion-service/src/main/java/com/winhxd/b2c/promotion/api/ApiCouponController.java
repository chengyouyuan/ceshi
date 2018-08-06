package com.winhxd.b2c.promotion.api;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.promotion.condition.CouponCondition;
import com.winhxd.b2c.common.domain.promotion.vo.CouponVO;
import com.winhxd.b2c.common.feign.promotion.ApiCouponServiceClient;
import com.winhxd.b2c.promotion.service.CouponService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther wangxiaoshun
 * @Date 2018/8/6 10 39
 * @Description 获取优惠券相关接口
 */
@RestController
@Api(tags = "ApiCoupon")
@RequestMapping(value = "/api-coupon/coupon")
public class ApiCouponController implements ApiCouponServiceClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiCouponController.class);

    @Resource
    private CouponService couponService;

    @Override
    @ApiOperation(value = "新人专享优惠列表", response = Boolean.class, notes = "新人专享优惠列表")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = Boolean.class),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")
    })
    @RequestMapping(value = "/501/v1/getNewUserCouponList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<List<CouponVO>> getNewUserCouponList(@RequestBody CouponCondition couponCondition) {
        LOGGER.info("=/api-coupon/coupon/501/v1/getNewUserCouponList-查询新人专享优惠列表=--开始--{}", couponCondition);
        ResponseResult<List<CouponVO>> result = new ResponseResult<>();
        try {
            List<CouponVO> couponVOs =  couponService.getNewUserCouponList(couponCondition);
            result.setData(couponVOs);
        } catch (Exception e) {
            LOGGER.error("=/api-coupon/coupon/501/v1/getNewUserCouponList-查询新人专享优惠列表=--异常" + e, e);
            result.setCode(BusinessCode.CODE_1001);
        }
        LOGGER.info("=/api-coupon/coupon/501/v1/getNewUserCouponList-查询新人专享优惠列表=--结束 result={}", result);
        return result;
    }

    @Override
    @ApiOperation(value = "待领取优惠券列表", response = Boolean.class, notes = "待领取优惠券列表")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = Boolean.class),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")
    })
    @RequestMapping(value = "/502/v1/unclaimedCouponList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<PagedList<CouponVO>> unclaimedCouponList(@RequestBody CouponCondition couponCondition) {
        LOGGER.info("=/api-coupon/coupon/502/v1/unclaimedCouponList-待领取优惠券列表=--开始--{}", couponCondition);
        ResponseResult<CouponVO> result = new ResponseResult<>();
        try {
            //返回对象
        } catch (Exception e) {
            LOGGER.error("=/api-coupon/coupon/502/v1/unclaimedCouponList-待领取优惠券列表=--异常" + e, e);
            result.setCode(BusinessCode.CODE_1001);
        }
        LOGGER.info("=/api-coupon/coupon/502/v1/unclaimedCouponList-待领取优惠券列表=--结束 result={}", result);
        return null;
    }

    @Override
    @ApiOperation(value = "我的优惠券列表", response = Boolean.class, notes = "我的优惠券列表")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = Boolean.class),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")
    })
    @RequestMapping(value = "/503/v1/myCouponList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<PagedList<CouponVO>> myCouponList(@RequestBody CouponCondition couponCondition) {
        LOGGER.info("=/api-coupon/coupon/503/v1/myCouponList-我的优惠券列表=--开始--{}", couponCondition);
        ResponseResult<CouponVO> result = new ResponseResult<>();
        try {
            //返回对象
        } catch (Exception e) {
            LOGGER.error("=/api-coupon/coupon/503/v1/myCouponList-我的优惠券列表=--异常" + e, e);
            result.setCode(BusinessCode.CODE_1001);
        }
        LOGGER.info("=/api-coupon/coupon/503/v1/myCouponList-我的优惠券列表=--结束 result={}", result);
        return null;
    }


    @Override
    @ApiOperation(value = "用户领取优惠券", response = Boolean.class, notes = "用户领取优惠券")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = Boolean.class),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")
    })
    @RequestMapping(value = "/504/v1/userReceiveCoupon", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<Boolean> userReceiveCoupon(@RequestBody CouponCondition couponCondition) {
        LOGGER.info("=/api-coupon/coupon/504/v1/userReceiveCoupon-用户领取优惠券=--开始--{}", couponCondition);
        ResponseResult<Boolean> result = new ResponseResult<>();
        try {
            //返回对象
            result.setData(null);
        } catch (Exception e) {
            LOGGER.error("=/api-coupon/coupon/504/v1/userReceiveCoupon-用户领取优惠券=--异常" + e, e);
            result.setCode(BusinessCode.CODE_1001);
        }
        LOGGER.info("=/api-coupon/coupon/504/v1/userReceiveCoupon-用户领取优惠券=--结束 result={}", result);
        return result;
    }

    @Override
    @ApiOperation(value = "根据订单查询优惠券列表", response = Boolean.class, notes = "根据订单查询优惠券列表")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = Boolean.class),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")
    })
    @RequestMapping(value = "/505/v1/couponListByOrder", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<PagedList<CouponVO>> couponListByOrder(@RequestBody CouponCondition couponCondition) {
        LOGGER.info("=/api-coupon/coupon/504/v1/couponListByOrder-根据订单查询优惠券列表=--开始--{}", couponCondition);
        ResponseResult<PagedList<CouponVO>> result = new ResponseResult<>();
        try {
            //返回对象
            result.setData(null);
        } catch (Exception e) {
            LOGGER.error("=/api-coupon/coupon/505/v1/couponListByOrder-根据订单查询优惠券列表=--异常" + e, e);
            result.setCode(BusinessCode.CODE_1001);
        }
        LOGGER.info("=/api-coupon/coupon/505/v1/couponListByOrder-根据订单查询优惠券列表=--结束 result={}", result);
        return result;
    }
}
