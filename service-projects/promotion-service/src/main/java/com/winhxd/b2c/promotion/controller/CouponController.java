package com.winhxd.b2c.promotion.controller;

import com.winhxd.b2c.common.domain.promotion.condition.CouponCondition;
import com.winhxd.b2c.promotion.service.CouponService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.promotion.vo.CouponActivityVO;
import com.winhxd.b2c.common.feign.promotion.CouponServiceClient;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.annotation.Resource;

@Api(tags = "coupon")
@RestController
public class CouponController implements CouponServiceClient{
	private static final Logger LOGGER = LoggerFactory.getLogger(CouponController.class);

	@Resource
	private CouponService couponService;
	
	@Override
	@ApiOperation(value = "获取门店用户领取优惠券数量", response = CouponActivityVO.class, notes = "获取门店用户领取优惠券数量")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = String.class),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")})
	public ResponseResult<String> getCouponNumsByCustomerForStore(Long storeId,Long customerId) {
		return null;
	}


	@Override
	@ApiOperation(value = "订单使用优惠券", response = Boolean.class, notes = "订单使用优惠券")
	@ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = Boolean.class),
			@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")
	})
	public ResponseResult<Boolean> orderUseCoupon(@RequestBody CouponCondition condition) {
		LOGGER.info("=/coupon/orderUseCoupon-订单使用优惠券=--开始--{}", condition);
		ResponseResult<Boolean> result = new ResponseResult<>();
		try {
			Boolean flag =  couponService.orderUseCoupon(condition);
			result.setData(flag);
		} catch (Exception e) {
			LOGGER.error("=/coupon/orderUseCoupon-订单使用优惠券=--异常" + e, e);
			result.setCode(BusinessCode.CODE_1001);
		}
		LOGGER.info("=/coupon/orderUseCoupon-订单使用优惠券=--结束 result={}", result);
		return result;
	}

	@Override
	@ApiOperation(value = "订单退回优惠券", response = Boolean.class, notes = "订单退回优惠券")
	@ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = Boolean.class),
			@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")
	})
	public ResponseResult<Boolean> orderUntreadCoupon(CouponCondition condition) {
		LOGGER.info("=/coupon/orderUntreadCoupon-订单退回优惠券=--开始--{}", condition);
		ResponseResult<Boolean> result = new ResponseResult<>();
		try {
			Boolean flag =  couponService.orderUntreadCoupon(condition);
			result.setData(flag);
		} catch (Exception e) {
			LOGGER.error("=/coupon/orderUntreadCoupon-订单退回优惠券=--异常" + e, e);
			result.setCode(BusinessCode.CODE_1001);
		}
		LOGGER.info("=/coupon/orderUntreadCoupon-订单退回优惠券=--结束 result={}", result);
		return result;
	}

	@Override
	@ApiOperation(value = "撤回优惠券", response = Boolean.class, notes = "撤回优惠券")
	@ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = Boolean.class),
			@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")
	})
	public ResponseResult<Boolean> revokeCoupon(CouponCondition condition) {
		LOGGER.info("=/coupon/revokeCoupon-撤回优惠券=--开始--{}", condition);
		ResponseResult<Boolean> result = new ResponseResult<>();
		try {
			Boolean flag =  couponService.revokeCoupon(condition);
			result.setData(flag);
		} catch (Exception e) {
			LOGGER.error("=/coupon/revokeCoupon-撤回优惠券=--异常" + e, e);
			result.setCode(BusinessCode.CODE_1001);
		}
		LOGGER.info("=/coupon/revokeCoupon-撤回优惠券=--结束 result={}", result);
		return result;
	}

}
