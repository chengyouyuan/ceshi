package com.winhxd.b2c.promotion.api;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.common.ApiCondition;
import com.winhxd.b2c.common.domain.promotion.condition.CouponCondition;
import com.winhxd.b2c.common.domain.promotion.condition.CouponPreAmountCondition;
import com.winhxd.b2c.common.domain.promotion.condition.ReceiveCouponCondition;
import com.winhxd.b2c.common.domain.promotion.vo.CouponInStoreGetedAndUsedVO;
import com.winhxd.b2c.common.domain.promotion.vo.CouponVO;
import com.winhxd.b2c.promotion.service.CouponService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_500002, message = "该手机号已经享受过新用户福利"),
            @ApiResponse(code = BusinessCode.CODE_500001, message = "不存在符合的优惠券活动")
    })
    @RequestMapping(value = "/501/v1/getNewUserCouponList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<List<CouponVO>> getNewUserCouponList(@RequestBody ApiCondition condition) {
        LOGGER.info("=/api-promotion/coupon/501/v1/getNewUserCouponList-查询新人专享优惠列表=--开始--{}");
        ResponseResult<List<CouponVO>> result = new ResponseResult<>();
        List<CouponVO> couponVOs =  couponService.getNewUserCouponList();
        result.setData(couponVOs);
        LOGGER.info("=/api-promotion/coupon/501/v1/getNewUserCouponList-查询新人专享优惠列表=--结束 result={}", result);
        return result;
    }

    @ApiOperation(value = "待领取优惠券列表", notes = "待领取优惠券列表")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_410001, message = "用户不存在")
    })
    @RequestMapping(value = "/502/v1/unclaimedCouponList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<List<CouponVO>> unclaimedCouponList(@RequestBody ApiCondition condition) {
        LOGGER.info("=/api-promotion/coupon/502/v1/unclaimedCouponList-待领取优惠券列表=--开始--{}");
        ResponseResult<List<CouponVO>> result = new ResponseResult<>();
        List<CouponVO> pages = couponService.unclaimedCouponList();
        result.setData(pages);
        LOGGER.info("=/api-promotion/coupon/502/v1/unclaimedCouponList-待领取优惠券列表=--结束 result={}", result);
        return result;
    }

    @ApiOperation(value = "我的优惠券列表", notes = "我的优惠券列表")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_410001, message = "用户不存在")
    })
    @RequestMapping(value = "/503/v1/myCouponList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<PagedList<CouponVO>> myCouponList(@RequestBody CouponCondition couponCondition) {
        LOGGER.info("=/api-promotion/coupon/503/v1/myCouponList-我的优惠券列表=--开始--{}", couponCondition);
        ResponseResult<PagedList<CouponVO>> result = new ResponseResult<>();
        PagedList<CouponVO> pages = couponService.myCouponList(couponCondition);
        result.setData(pages);
        LOGGER.info("=/api-promotion/coupon/503/v1/myCouponList-我的优惠券列表=--结束 result={}", result);
        return result;
    }


    @ApiOperation(value = "用户领取优惠券", notes = "用户领取优惠券")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_410001, message = "用户不存在")
    })
    @RequestMapping(value = "/504/v1/userReceiveCoupon", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<Boolean> userReceiveCoupon(@RequestBody ReceiveCouponCondition condition) {
        LOGGER.info("=/api-promotion/coupon/504/v1/userReceiveCoupon-用户领取优惠券=--开始--{}", condition);
        ResponseResult<Boolean> result = new ResponseResult<>();
        Boolean flag =  couponService.userReceiveCoupon(condition);
        result.setData(flag);
        LOGGER.info("=/api-promotion/coupon/504/v1/userReceiveCoupon-用户领取优惠券=--结束 result={}", result);
        return result;
    }

    @ApiOperation(value = "获取用户可领取门店优惠券种类数", notes = "获取用户可领取门店优惠券种类数")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_410001, message = "用户不存在")
    })
    @RequestMapping(value = "/508/v1/getStoreCouponKinds", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseResult<Integer> getStoreCouponKinds(ApiCondition condition){
        logTitle="=/api-promotion/coupon/508/v1/getStoreCouponKinds-获取用户可领取门店优惠券种类数=--";
        LOGGER.info(logTitle+"开始--{}--");
        ResponseResult<Integer> result = new ResponseResult<>();
        Integer StoreCouponKinds = couponService.getStoreCouponKinds();
        result.setData(StoreCouponKinds);
        LOGGER.info(logTitle+"结束 result={}", result);
        return result;
    }
    @ApiOperation(value = "用户查询门店优惠券列表", notes = "用户查询门店优惠券列表")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_410001, message = "用户不存在")
    })
    @RequestMapping(value = "/509/v1/getStoreCouponList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseResult<List<CouponVO>> getStoreCouponList(ApiCondition condition){
        logTitle="=/api-promotion/coupon/509/v1/getStoreCouponList-用户查询门店优惠券列表=--";
        LOGGER.info(logTitle+"开始--{}");
        ResponseResult<List<CouponVO>> result = new ResponseResult<>();
        List<CouponVO> pages = couponService.findStoreCouponList();
        result.setData(pages);
        LOGGER.info(logTitle+"结束 result={}", result);
        return result;
    }

    @ApiOperation(value = "订单可用的优惠券列表", notes = "订单可用的优惠券列表")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")
    })
    @RequestMapping(value = "/545/v1/availableCouponListByOrder", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseResult<List<CouponVO>> availableCouponListByOrder(@RequestBody CouponPreAmountCondition couponCondition){
        LOGGER.info("=/api-promotion/coupon/509/v1/availableCouponListByOrder-订单可用的优惠券列表=--开始--{}");
        ResponseResult<List<CouponVO>> result = new ResponseResult<>();
        List<CouponVO> pages = couponService.availableCouponListByOrder(couponCondition);
        result.setData(pages);
        LOGGER.info("=/api-promotion/coupon/509/v1/availableCouponListByOrder-订单可用的优惠券列表=--结束 result={}", result);
        return result;
    }



    /***
    @ApiOperation(value = "门店优惠券列表领取使/用情况统计", notes = "门店优惠券列表领取使/用情况统计")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")
    })
    @RequestMapping(value = "/547/v1/getCouponInStoreGetedAndUsedPage", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<PagedList<CouponInStoreGetedAndUsedVO>> findCouponInStoreGetedAndUsedPage(@RequestParam("storeId") Long storeId,@RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize){
        LOGGER.info("=/api-promotion/coupon/547/v1/getCouponInStoreGetedAndUsedPage"+ "门店ID: "+storeId +"pageNo:"+pageNo +"pageSize:"+pageSize);
        if(pageNo==null){
            pageNo = 1 ;
        }
        if(pageSize==null){
            pageNo = 10;
        }
        ResponseResult<PagedList<CouponInStoreGetedAndUsedVO>> result = new ResponseResult<>();
        PagedList<CouponInStoreGetedAndUsedVO> pages = couponService.findCouponInStoreGetedAndUsedPage(storeId,pageNo,pageSize);
        result.setData(pages);
        LOGGER.info("/api-promotion/coupon/547/v1/getCouponInStoreGetedAndUsedPage结果:"+result);
        return result;
    }
    ***/

}
