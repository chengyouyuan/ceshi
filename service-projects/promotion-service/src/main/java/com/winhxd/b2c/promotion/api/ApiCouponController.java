package com.winhxd.b2c.promotion.api;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.promotion.condition.*;
import com.winhxd.b2c.common.domain.promotion.vo.CouponInfoVO;
import com.winhxd.b2c.common.domain.promotion.vo.CouponVO;
import com.winhxd.b2c.common.exception.BusinessException;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Auther wangxiaoshun
 * @Date 2018/8/6 10 39
 * @Description 获取优惠券相关接口
 */
@RestController
@Api(tags = "ApiCoupon")
@RequestMapping(value = "/api-promotion/coupon")
public class ApiCouponController{
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiCouponController.class);

    @Resource
    private CouponService couponService;

    private String logTitle=""; 
    @ApiOperation(value = "新人专享优惠列表", notes = "新人专享优惠列表")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")
    })
    @RequestMapping(value = "/501/v1/getNewUserCouponList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<List<CouponVO>> getNewUserCouponList() {
        LOGGER.info("=/api-promotion/coupon/501/v1/getNewUserCouponList-查询新人专享优惠列表=--开始--{}");
        ResponseResult<List<CouponVO>> result = new ResponseResult<>();
        try {
            List<CouponVO> couponVOs =  couponService.getNewUserCouponList();
            result.setData(couponVOs);
        }catch (BusinessException e){
            LOGGER.error("=/api-promotion/coupon/501/v1/getNewUserCouponList-查询新人专享优惠列表=--异常" + e, e);
            result.setCode(e.getErrorCode());
        }catch (Exception e) {
            LOGGER.error("=/api-promotion/coupon/501/v1/getNewUserCouponList-查询新人专享优惠列表=--异常" + e, e);
            result.setCode(BusinessCode.CODE_1001);
        }
        LOGGER.info("=/api-promotion/coupon/501/v1/getNewUserCouponList-查询新人专享优惠列表=--结束 result={}", result);
        return result;
    }

    @ApiOperation(value = "待领取优惠券列表", notes = "待领取优惠券列表")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")
    })
    @RequestMapping(value = "/502/v1/unclaimedCouponList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<List<CouponVO>> unclaimedCouponList() {
        LOGGER.info("=/api-promotion/coupon/502/v1/unclaimedCouponList-待领取优惠券列表=--开始--{}");
        ResponseResult<List<CouponVO>> result = new ResponseResult<>();
        try {
            List<CouponVO> pages = couponService.unclaimedCouponList();
            result.setData(pages);
        }catch (BusinessException e){
            LOGGER.error("=/api-promotion/coupon/501/v1/unclaimedCouponList-待领取优惠券列表=--异常" + e, e);
            result.setCode(e.getErrorCode());
        } catch (Exception e) {
            LOGGER.error("=/api-promotion/coupon/502/v1/unclaimedCouponList-待领取优惠券列表=--异常" + e, e);
            result.setCode(BusinessCode.CODE_1001);
        }
        LOGGER.info("=/api-promotion/coupon/502/v1/unclaimedCouponList-待领取优惠券列表=--结束 result={}", result);
        return result;
    }

    @ApiOperation(value = "我的优惠券列表", notes = "我的优惠券列表")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")
    })
    @RequestMapping(value = "/503/v1/myCouponList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<PagedList<CouponVO>> myCouponList(@RequestBody CouponCondition couponCondition) {
        LOGGER.info("=/api-promotion/coupon/503/v1/myCouponList-我的优惠券列表=--开始--{}", couponCondition);
        ResponseResult<PagedList<CouponVO>> result = new ResponseResult<>();
        try {
            PagedList<CouponVO> pages = couponService.myCouponList(couponCondition);
            result.setData(pages);
        } catch (BusinessException e){
            LOGGER.error("=/api-promotion/coupon/501/v1/myCouponList-我的优惠券列表=--异常" + e, e);
            result.setCode(e.getErrorCode());
        } catch (Exception e) {
            LOGGER.error("=/api-promotion/coupon/503/v1/myCouponList-我的优惠券列表=--异常" + e, e);
            result.setCode(BusinessCode.CODE_1001);
        }
        LOGGER.info("=/api-promotion/coupon/503/v1/myCouponList-我的优惠券列表=--结束 result={}", result);
        return result;
    }


    @ApiOperation(value = "用户领取优惠券", notes = "用户领取优惠券")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")
    })
    @RequestMapping(value = "/504/v1/userReceiveCoupon", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<Boolean> userReceiveCoupon(@RequestBody ReceiveCouponCondition condition) {
        LOGGER.info("=/api-promotion/coupon/504/v1/userReceiveCoupon-用户领取优惠券=--开始--{}", condition);
        ResponseResult<Boolean> result = new ResponseResult<>();
        try {
            //返回对象
            Boolean flag =  couponService.userReceiveCoupon(condition);
            result.setData(flag);
        } catch (BusinessException e){
            LOGGER.error("=/api-promotion/coupon/501/v1/userReceiveCoupon-用户领取优惠券=--异常" + e, e);
            result.setCode(e.getErrorCode());
        } catch (Exception e) {
            LOGGER.error("=/api-promotion/coupon/504/v1/userReceiveCoupon-用户领取优惠券=--异常" + e, e);
            result.setCode(BusinessCode.CODE_1001);
        }
        LOGGER.info("=/api-promotion/coupon/504/v1/userReceiveCoupon-用户领取优惠券=--结束 result={}", result);
        return result;
    }
    @ApiOperation(value = "获取优惠券详情", notes = "获取优惠券详情")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")
    })
    @RequestMapping(value = "/506/v1/getCouponInfoByTemplateId", method = RequestMethod.GET)
	ResponseResult<CouponInfoVO> getCouponInfoByTemplateId(Long couponTemPlateId){
    	logTitle="=/api-promotion/coupon/506/v1/getCouponInfoByTemplateId-获取优惠券详情=--";
    	LOGGER.info(logTitle+"开始--{}", couponTemPlateId);
        ResponseResult<CouponInfoVO> result = new ResponseResult<>();
        try {
            //返回对象
            result.setData(null);
        } catch (Exception e) {
            LOGGER.error(logTitle+"异常" + e, e);
            result.setCode(BusinessCode.CODE_1001);
        }
        LOGGER.info(logTitle+"结束 result={}", result);
        return result;
    }
    @ApiOperation(value = "检查用户优惠券是否可用", notes = "检查用户优惠券是否可用")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")
    })
	@RequestMapping(value = "/507/v1/checkCouponStatus", method = RequestMethod.POST)
	ResponseResult<String> checkCouponStatus(@RequestBody CouponCheckStatusCondition condition){
		logTitle="=/api-promotion/coupon/507/v1/checkCouponStatus-检查用户优惠券是否可用=--";
    	LOGGER.info(logTitle+"开始--{}", condition);
        ResponseResult<String> result = new ResponseResult<>();
        try {
            //返回对象
            result.setData(null);
        } catch (Exception e) {
            LOGGER.error(logTitle+"异常" + e, e);
            result.setCode(BusinessCode.CODE_1001);
        }
        LOGGER.info(logTitle+"结束 result={}", result);
        return result;
	}
    @ApiOperation(value = "获取用户可领取门店优惠券种类数", notes = "获取用户可领取门店优惠券种类数")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")
    })
	@RequestMapping(value = "/508/v1/getStoreCouponKinds", method = RequestMethod.GET)
	ResponseResult<String> getStoreCouponKinds(@RequestParam("storeId") Long storeId,@RequestParam("customerId") Long customerId){
		logTitle="=/api-promotion/coupon/508/v1/getStoreCouponKinds-获取用户可领取门店优惠券种类数=--";
    	LOGGER.info(logTitle+"开始--{}--","storeId="+ storeId+"--customerId="+customerId);
        ResponseResult<String> result = new ResponseResult<>();
        try {
            //返回对象
            result.setData(null);
        } catch (Exception e) {
            LOGGER.error(logTitle+"异常" + e, e);
            result.setCode(BusinessCode.CODE_1001);
        }
        LOGGER.info(logTitle+"结束 result={}", result);
        return result;
	}
    @ApiOperation(value = "用户查询门店优惠券列表", notes = "用户查询门店优惠券列表")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")
    })
	@RequestMapping(value = "/509/v1/getStoreCouponList", method = RequestMethod.GET)
	ResponseResult<CouponInfoVO> getStoreCouponList(@RequestBody CouponInfoCondition condition){
		logTitle="=/api-promotion/coupon/509/v1/getStoreCouponList-用户查询门店优惠券列表=--";
    	LOGGER.info(logTitle+"开始--{}", condition);
        ResponseResult<CouponInfoVO> result = new ResponseResult<>();
        try {
            //返回对象
            result.setData(null);
        } catch (Exception e) {
            LOGGER.error(logTitle+"异常" + e, e);
            result.setCode(BusinessCode.CODE_1001);
        }
        LOGGER.info(logTitle+"结束 result={}", result);
        return result;
	}
}
