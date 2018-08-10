package com.winhxd.b2c.promotion.controller;

import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.promotion.condition.*;
import com.winhxd.b2c.common.domain.promotion.vo.CouponDiscountVO;
import com.winhxd.b2c.common.domain.promotion.vo.CouponVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.promotion.service.CouponService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.List;

@Api(tags = "coupon")
@RestController
public class CouponController implements CouponServiceClient{
	private static final Logger LOGGER = LoggerFactory.getLogger(CouponController.class);

	@Autowired
	private CouponService couponService;
	
	@Override
	@ApiOperation(value = "获取门店用户领取优惠券数量", notes = "获取门店用户领取优惠券数量")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")})
	public ResponseResult<String> getCouponNumsByCustomerForStore(Long storeId,Long customerId) {
		return null;
	}


	@Override
	@ApiOperation(value = "订单使用优惠券", notes = "订单使用优惠券")
	@ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
			@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")
	})
	public ResponseResult<Boolean> orderUseCoupon(@RequestBody OrderUseCouponCondition condition) {
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
	@ApiOperation(value = "订单退回优惠券",notes = "订单退回优惠券")
	@ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
			@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")
	})
	public ResponseResult<Boolean> orderUntreadCoupon(@RequestBody OrderUntreadCouponCondition condition) {
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
	@ApiOperation(value = "撤回优惠券", notes = "撤回优惠券")
	@ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
			@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")
	})
	public ResponseResult<Boolean> revokeCoupon(@RequestBody RevokeCouponCodition condition) {
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

	@Override
	@ApiOperation(value = "订单已使用的优惠券列表", notes = "订单已使用的优惠券列表")
	@ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
			@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")
	})
	public ResponseResult<List<CouponVO>> couponListByOrder(@RequestBody OrderCouponCondition couponCondition) {
		LOGGER.info("=/coupon/couponListByOrder-查询订单使用的优惠券列表=--开始--{}", couponCondition);
		ResponseResult<List<CouponVO>> result = new ResponseResult<>();
		try {
            List<CouponVO> pages = couponService.couponListByOrder(couponCondition);
			result.setData(pages);
		} catch (BusinessException e) {
            LOGGER.error("=/coupon/couponListByOrder-查询订单使用的优惠券列表=--异常" + e, e);
            result.setCode(e.getErrorCode());
        }
		catch (Exception e) {
			LOGGER.error("=/coupon/couponListByOrder-查询订单使用的优惠券列表=--异常" + e, e);
			result.setCode(BusinessCode.CODE_1001);
		}
		LOGGER.info("=/coupon/couponListByOrder-查询订单使用的优惠券列表=--结束 result={}", result);
		return result;
	}

	@Override
	@ApiOperation(value = "订单可用的优惠券列表", notes = "订单可用的优惠券列表")
	@ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
			@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")
	})
	public ResponseResult<List<CouponVO>> availableCouponListByOrder(@RequestBody CouponProductCondition couponCondition) {
		LOGGER.info("=/coupon/availableCouponListByOrder-订单可用的优惠券列表=--开始--{}", couponCondition);
		ResponseResult<List<CouponVO>> result = new ResponseResult<>();
		try {
			//返回对象
		}catch (Exception e) {
			LOGGER.error("=/coupon/availableCouponListByOrder-订单可用的优惠券列表=--异常" + e, e);
			result.setCode(BusinessCode.CODE_1001);
		}
		LOGGER.info("=/coupon/availableCouponListByOrder-订单可用的优惠券列表=--结束 result={}", result);
		return result;
	}

	@Override
	@ApiOperation(value = "计算订单优惠金额", notes = "计算订单优惠金额")
	@ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
			@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")
	})
	public ResponseResult<CouponDiscountVO> couponDiscountAmount(@RequestBody CouponPreAmountCondition couponCondition) {
		LOGGER.info("=/coupon/couponDiscountAmount-订单可用的优惠券列表=--开始--{}", couponCondition);
		ResponseResult<CouponDiscountVO> result = new ResponseResult<>();
		try {
			//返回对象
		}catch (Exception e) {
			LOGGER.error("=/coupon/couponDiscountAmount-订单可用的优惠券列表=--异常" + e, e);
			result.setCode(BusinessCode.CODE_1001);
		}
		LOGGER.info("=/coupon/couponDiscountAmount-订单可用的优惠券列表=--结束 result={}", result);
		return result;
	}

}
