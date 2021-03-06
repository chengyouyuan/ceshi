package com.winhxd.b2c.promotion.controller;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.context.UserContext;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.promotion.condition.*;
import com.winhxd.b2c.common.domain.promotion.vo.CouponDiscountVO;
import com.winhxd.b2c.common.domain.promotion.vo.CouponInvestorAmountVO;
import com.winhxd.b2c.common.domain.promotion.vo.CouponVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.promotion.CouponServiceClient;
import com.winhxd.b2c.promotion.service.CouponPushService;
import com.winhxd.b2c.promotion.service.CouponService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "coupon")
@RestController
public class CouponController implements CouponServiceClient{
	private static final Logger LOGGER = LoggerFactory.getLogger(CouponController.class);

	@Autowired
	private CouponService couponService;
	@Autowired
	private CouponPushService couponPushService;

	@Override
	@ApiOperation(value = "获取门店用户领取优惠券数量", notes = "获取门店用户领取优惠券数量")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
    @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")})
	public ResponseResult<String> getCouponNumsByCustomerForStore( @RequestParam("customerId")Long customerId) {
		ResponseResult<String> result = new ResponseResult<String>();
		Integer count= couponService.getCouponNumsByCustomerForStore(customerId);
		if(count==null){
			count = 0;
		}
		result.setData(String.valueOf(count));
		return result;
	}


	@Override
	@ApiOperation(value = "订单使用优惠券", notes = "订单使用优惠券")
	@ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
			@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")
	})
	public ResponseResult<Boolean> orderUseCoupon(@RequestBody OrderUseCouponCondition condition) {
		LOGGER.info("=/coupon/orderUseCoupon-订单使用优惠券=--开始--{}--订单号{}", condition,condition.getOrderNo());

		if (CollectionUtils.isEmpty(condition.getSendIds()) || condition.getCouponPrice() == null
				|| null == condition.getOrderNo() || null == condition.getOrderPrice()) {
			throw new BusinessException(BusinessCode.CODE_1007);
		}

		ResponseResult<Boolean> result = new ResponseResult<>();
		Boolean flag =  couponService.orderUseCoupon(condition,UserContext.getCurrentCustomerUser());
		result.setData(flag);
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

		if (StringUtils.isBlank(condition.getOrderNo())) {
			LOGGER.error("CouponController.orderUntreadCoupon-订单号为空");
			throw new BusinessException(BusinessCode.CODE_1007);
		}
		ResponseResult<Boolean> result = new ResponseResult<>();
		Boolean flag =  couponService.orderUntreadCoupon(condition);
		result.setData(flag);
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
		if (CollectionUtils.isEmpty(condition.getSendIds())) {
			LOGGER.error("CouponController.revokeCoupon 优惠券发放id为空");
			throw new BusinessException(BusinessCode.CODE_1007);
		}
		ResponseResult<Boolean> result = new ResponseResult<>();
		Boolean flag =  couponService.revokeCoupon(condition);
		result.setData(flag);
		LOGGER.info("=/coupon/revokeCoupon-撤回优惠券=--结束 result={}", result);
		return result;
	}

	@Override
	@ApiOperation(value = "订单已使用的优惠券列表", notes = "订单已使用的优惠券列表")
	@ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
			@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
			@ApiResponse(code = BusinessCode.CODE_1007, message = "参数无效")

	})
	public ResponseResult<List<CouponVO>> couponListByOrder(@RequestBody OrderCouponCondition couponCondition) {
		LOGGER.info("=/coupon/couponListByOrder-查询订单使用的优惠券列表=--开始--{}", couponCondition);

		if (StringUtils.isBlank(couponCondition.getOrderNo())) {
			LOGGER.error("CouponController.couponCondition-订单号为空");
			throw new BusinessException(BusinessCode.CODE_1007);
		}

		ResponseResult<List<CouponVO>> result = new ResponseResult<>();
		List<CouponVO> pages = couponService.couponListByOrder(couponCondition);
		result.setData(pages);
		LOGGER.info("=/coupon/couponListByOrder-查询订单使用的优惠券列表=--结束 result={}", result);
		return result;
	}


	@Override
	@ApiOperation(value = "计算订单优惠金额", notes = "计算订单优惠金额")
	@ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
			@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")
	})
	public ResponseResult<CouponDiscountVO> couponDiscountAmount(@RequestBody CouponPreAmountCondition couponCondition) {
		LOGGER.info("=/coupon/couponDiscountAmount-订单可用的优惠券列表=--开始--{}", couponCondition);

		if (CollectionUtils.isEmpty(couponCondition.getSendIds()) || CollectionUtils.isEmpty(couponCondition.getProducts())) {
			LOGGER.error("CouponController.couponDiscountAmount 参数错误");
			throw new BusinessException(BusinessCode.CODE_1007);
		}

		ResponseResult<CouponDiscountVO> result = new ResponseResult<>();
		CouponDiscountVO couponDiscountVO = couponService.couponDiscountAmount(couponCondition);
		result.setData(couponDiscountVO);
		LOGGER.info("=/coupon/couponDiscountAmount-订单可用的优惠券列表=--结束 result={}", result);
		return result;
	}

	/**
	 * 检查用户优惠券是否可用
	 * sjx
	 * @param condition
	 * @return
	 */
	@Override
	@ApiOperation(value = "检查用户优惠券是否可用", notes = "检查用户优惠券是否可用")
	@ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
			@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")
	})
	public ResponseResult<Boolean> checkCouponStatus(@RequestBody CouponCheckStatusCondition condition){
		LOGGER.info("=/promotion/5007/v1/checkCouponStatus-检查用户优惠券是否可用=--开始--{}", condition);
		if(condition.getSendId() == null){
			throw new BusinessException(BusinessCode.CODE_1007);
		}
		ResponseResult<Boolean> result = new ResponseResult<>();
		Boolean flag = couponService.checkCouponStatus(condition);
		result.setData(flag);
		LOGGER.info("=/promotion/5007/v1/checkCouponStatus-检查用户优惠券是否可用=--结束 result={}", result);
		return result;
	}

	@Override
	@ApiOperation(value = "根据订单获取优惠券出资方费用承担信息", notes = "根据订单获取优惠券出资方费用承担信息")
	@ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
			@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")
	})
	public ResponseResult<List<CouponInvestorAmountVO>> getCouponInvestorAmount(@RequestBody CouponInvestorAmountCondition condition) {
		LOGGER.info("=/promotion/5046/v1/getCouponInvestorAmount-根据订单获取优惠券费用承担信息=--开始--{}", condition);

		if (CollectionUtils.isEmpty(condition.getOrderNos())) {
			throw new BusinessException(BusinessCode.CODE_1007);
		}

		ResponseResult<List<CouponInvestorAmountVO>> result = new ResponseResult<>();
		List<CouponInvestorAmountVO>  couponInvestorAmountVOs = couponService.getCouponInvestorAmount(condition);
		result.setData(couponInvestorAmountVOs);
		LOGGER.info("=/promotion/5046/v1/getCouponInvestorAmount-根据订单获取优惠券费用承担信息=--结束 result={}", result);
		return result;
	}

	@Override
	@ApiOperation(value = "获取最优惠的优惠券", notes = "最优惠的优惠券")
	@ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
			@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")
	})
	public ResponseResult<CouponVO> findDefaultCoupon(@RequestBody OrderAvailableCouponCondition condition) {
		LOGGER.info("=/promotion/5058/v1/findDefaultCoupon-最优惠的优惠券=--开始--{}", condition);
		// 校验参数
		if (null == condition.getStoreId() || StringUtils.isBlank(condition.getPayType())) {
			LOGGER.error("=/api-promotion/coupon//5048/v1/findDefaultCouponByOrder 参数错误");
			throw new BusinessException(BusinessCode.CODE_1007);
		}

		ResponseResult<CouponVO> result = new ResponseResult<>();
		CouponVO couponVO = couponService.findDefaultCouponByOrder(condition,UserContext.getCurrentCustomerUser());
		result.setData(couponVO);
		LOGGER.info("=/promotion/5058/v1/findDefaultCoupon-最优惠的优惠券=--结束 result={}", result);
		return result;
	}

	@Override
	@ApiOperation(value = "检测用户是否有可推送的优惠券", notes = "检测用户是否有可推送的优惠券")
	@ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
			@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")
	})
	public ResponseResult<Boolean> checkCouponsAvailable(Long customerId) {
		LOGGER.info("=/promotion/5062/v1/checkCouponsAvailable-检测用户是否有可推送的优惠券=--开始用户ID--{}", customerId);
		ResponseResult<Boolean> result = new ResponseResult<>();
		boolean availableCoupon = couponPushService.getAvailableCoupon(customerId);
		result.setData(availableCoupon);
		LOGGER.info("=/promotion/5062/v1/checkCouponsAvailable-检测用户是否有可推送的优惠券=--结束-- result={}", result.getData());

		return result;
	}

}
