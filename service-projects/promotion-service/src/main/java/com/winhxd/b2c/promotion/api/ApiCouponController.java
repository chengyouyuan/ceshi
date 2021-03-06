package com.winhxd.b2c.promotion.api;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.context.CustomerUser;
import com.winhxd.b2c.common.context.StoreUser;
import com.winhxd.b2c.common.context.UserContext;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.common.ApiCondition;
import com.winhxd.b2c.common.domain.promotion.condition.*;
import com.winhxd.b2c.common.domain.promotion.vo.*;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.util.JsonUtil;
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
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

    @Autowired
    private CouponPushService couponPushService;

    @ApiOperation(value = "C端新人专享优惠列表", notes = "C端新人专享优惠列表")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_500002, message = "该手机号已经享受过新用户福利"),
            @ApiResponse(code = BusinessCode.CODE_500001, message = "不存在符合的优惠券活动")
    })
    @RequestMapping(value = "/5001/v1/getNewUserCouponList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<List<CouponVO>> getNewUserCouponList(@RequestBody ApiCondition condition) {
        LOGGER.info("=/api-promotion/coupon/5001/v1/getNewUserCouponList-查询新人专享优惠列表=--开始--{}");
        ResponseResult<List<CouponVO>> result = new ResponseResult<>();
        CustomerUser customerUser = UserContext.getCurrentCustomerUser();
        List<CouponVO> couponVOs =  couponService.getNewUserCouponList(customerUser);
        result.setData(couponVOs);
        LOGGER.info("=/api-promotion/coupon/5001/v1/getNewUserCouponList-查询新人专享优惠列表=--结束 result:{}", result);
        return result;
    }

    @ApiOperation(value = "C端待领取优惠券列表", notes = "C端待领取优惠券列表")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
    })
    @RequestMapping(value = "/5002/v1/unclaimedCouponList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<List<CouponVO>> unclaimedCouponList(@RequestBody ApiCondition condition) {
        LOGGER.info("=/api-promotion/coupon/5002/v1/unclaimedCouponList-待领取优惠券列表=--开始--{}");
        ResponseResult<List<CouponVO>> result = new ResponseResult<>();
        List<CouponVO> pages = couponService.unclaimedCouponList(UserContext.getCurrentCustomerUser());
        result.setData(pages);
        LOGGER.info("=/api-promotion/coupon/5002/v1/unclaimedCouponList-待领取优惠券列表=--结束 result:{}", result);
        return result;
    }

    @ApiOperation(value = "C端我的优惠券列表", notes = "C端我的优惠券列表")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")
    })
    @RequestMapping(value = "/5003/v1/myCouponList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<PagedList<CouponVO>> myCouponList(@RequestBody CouponCondition couponCondition) {
        LOGGER.info("=/api-promotion/coupon/5003/v1/myCouponList-我的优惠券列表=--开始--{}", JsonUtil.toJSONString(couponCondition));
        ResponseResult<PagedList<CouponVO>> result = new ResponseResult<>();
        PagedList<CouponVO> pages = couponService.myCouponList(couponCondition,UserContext.getCurrentCustomerUser());
        result.setData(pages);
        LOGGER.info("=/api-promotion/coupon/5003/v1/myCouponList-我的优惠券列表=--结束 result:{}", result);
        return result;
    }


    @ApiOperation(value = "C端用户领取优惠券", notes = "C端用户领取优惠券")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_500001, message = "不存在符合的优惠券活动"),
            @ApiResponse(code = BusinessCode.CODE_500017, message = "优惠券已领完")
    })
    @RequestMapping(value = "/5004/v1/userReceiveCoupon", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<Boolean> userReceiveCoupon(@RequestBody ReceiveCouponCondition condition) {
        LOGGER.info("=/api-promotion/coupon/5004/v1/userReceiveCoupon-用户领取优惠券=--开始--{}", JsonUtil.toJSONString(condition));
        ResponseResult<Boolean> result = new ResponseResult<>();
        Boolean flag = couponService.userReceiveCoupon(condition,UserContext.getCurrentCustomerUser());
        result.setData(flag);
        LOGGER.info("=/api-promotion/coupon/5004/v1/userReceiveCoupon-用户领取优惠券=--结束 result:{}", result);
        return result;
    }

    /**
     * 获取用户可领取门店优惠券种类数
     * sjx
     * @param condition
     * @return
     */
    @ApiOperation(value = "获取用户可领取门店优惠券种类数", notes = "获取用户可领取门店优惠券种类数")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")
    })
    @RequestMapping(value = "/5008/v1/getStoreCouponKinds", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseResult<CouponKindsVo> getStoreCouponKinds(ApiCondition condition){
        LOGGER.info("=/api-promotion/coupon/5008/v1/getStoreCouponKinds-获取用户可领取门店优惠券种类数=--开始--{}--");
        ResponseResult<CouponKindsVo> result = new ResponseResult<>();
        CouponKindsVo couponKindsVo = couponService.getStoreCouponKinds(UserContext.getCurrentCustomerUser());
        result.setData(couponKindsVo);
        LOGGER.info("=/api-promotion/coupon/5008/v1/getStoreCouponKinds-获取用户可领取门店优惠券种类数=--结束 result:{}", result);
        return result;
    }

    /**
     * 用户查询门店优惠券列表
     * @param condition
     * @return
     */
    @ApiOperation(value = "C端用户查询门店优惠券列表", notes = "C端用户查询门店优惠券列表")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")
    })
    @RequestMapping(value = "/5009/v1/getStoreCouponList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseResult<List<CouponVO>> getStoreCouponList(ApiCondition condition){
        LOGGER.info("=/api-promotion/coupon/5009/v1/getStoreCouponList-用户查询门店优惠券列表=--开始--{}");
        ResponseResult<List<CouponVO>> result = new ResponseResult<>();
        List<CouponVO> pages = couponService.findStoreCouponList(UserContext.getCurrentCustomerUser());
        result.setData(pages);
        LOGGER.info("=/api-promotion/coupon/5009/v1/getStoreCouponList-用户查询门店优惠券列表=--结束 result:{}", result);
        return result;
    }

    @ApiOperation(value = "C端订单可用的优惠券列表", notes = "C端订单可用的优惠券列表")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")
    })
    @RequestMapping(value = "/5045/v1/availableCouponListByOrder", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseResult<List<CouponVO>> availableCouponListByOrder(@RequestBody OrderAvailableCouponCondition couponCondition){
        LOGGER.info("=/api-promotion/coupon/5045/v1/availableCouponListByOrder-订单可用的优惠券列表=--开始--{}", JsonUtil.toJSONString(couponCondition));

        if (null == couponCondition.getStoreId() || StringUtils.isBlank(couponCondition.getPayType())) {
            LOGGER.error("=/api-promotion/coupon/5045/v1/availableCouponListByOrder 参数错误");
            throw new BusinessException(BusinessCode.CODE_1007);
        }
        ResponseResult<List<CouponVO>> result = new ResponseResult<>();
        List<CouponVO> pages = couponService.availableCouponListByOrder(couponCondition,UserContext.getCurrentCustomerUser());
        result.setData(pages);
        LOGGER.info("=/api-promotion/coupon/5045/v1/availableCouponListByOrder-订单可用的优惠券列表=--结束 result:{}", result);
        return result;
    }




    @ApiOperation(value = "门店优惠券列表领取使用情况统计", notes = "门店优惠券列表领取使用情况统计")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")
    })
    @RequestMapping(value = "/5047/v1/getCouponInStoreGetedAndUsedPage", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<PagedList<CouponInStoreGetedAndUsedVO>> findCouponInStoreGetedAndUsedPage(@RequestBody CouponInStoreGetedAndUsedCodition codition){
        StoreUser storeUser = UserContext.getCurrentStoreUser();
        Long storeId = storeUser.getBusinessId();
        LOGGER.info("=/api-promotion/coupon/5047/v1/getCouponInStoreGetedAndUsedPage=--开始--{},门店ID:{} ", JsonUtil.toJSONString(codition), storeId);
        ResponseResult<PagedList<CouponInStoreGetedAndUsedVO>> result = new ResponseResult<>();
        PagedList<CouponInStoreGetedAndUsedVO> pages = couponService.findCouponInStoreGetedAndUsedPage(storeId,codition);
        result.setData(pages);
        LOGGER.info("/api-promotion/coupon/5047/v1/getCouponInStoreGetedAndUsedPage结果 result:{}", result);
        return result;
    }

    @ApiOperation(value = "C端获取可用最优惠的优惠券", notes = "C端获取可用最优惠的优惠券")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")
    })
    @RequestMapping(value = "/5048/v1/findDefaultCouponByOrder", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<CouponVO> findDefaultCouponByOrder(@RequestBody OrderAvailableCouponCondition couponCondition){
        LOGGER.info("=/api-promotion/coupon/5048/v1/findDefaultCouponByOrder=--开始--{}", JsonUtil.toJSONString(couponCondition));

        if (null == couponCondition.getStoreId() || StringUtils.isBlank(couponCondition.getPayType())) {
            LOGGER.error("=/api-promotion/coupon//048/v1/findDefaultCouponByOrder 参数错误");
            throw new BusinessException(BusinessCode.CODE_1007);
        }
        ResponseResult<CouponVO> result = new ResponseResult<>();
        CouponVO couponVO = couponService.findDefaultCouponByOrder(couponCondition,UserContext.getCurrentCustomerUser());
        result.setData(couponVO);
        LOGGER.info("/api-promotion/coupon/5048/v1/findDefaultCouponByOrder结果 result:{}", result);
        return result;
    }

    @ApiOperation(value = "C端获取优惠券优惠金额", notes = "C端获取优惠券优惠金额")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")
    })
    @RequestMapping(value = "/5057/v1/getCouponDiscountAmount", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<CouponDiscountVO> getCouponDiscountAmount(@RequestBody CouponAmountCondition couponCondition){
        LOGGER.info("=/api-promotion/coupon/5057/v1/getCouponDiscountAmount=--开始--{}", JsonUtil.toJSONString(couponCondition));

        if (null == couponCondition || CollectionUtils.isEmpty(couponCondition.getSendIds())) {
            LOGGER.error("=/api-promotion/coupon/5057/v1/getCouponDiscountAmount 参数错误");
            throw new BusinessException(BusinessCode.CODE_1007);
        }
        ResponseResult<CouponDiscountVO> result = new ResponseResult<>();
        CouponDiscountVO couponDiscountVO  = couponService.getCouponDiscountAmount(couponCondition,UserContext.getCurrentCustomerUser());
        result.setData(couponDiscountVO);
        LOGGER.info("/api-promotion/coupon/5057/v1/getCouponDiscountAmount结果 result:{}", result);
        return result;
    }

    @ApiOperation(value = "C端校验是否有新用户活动", notes = "C端校验是否有新用户活动")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")
    })
    @RequestMapping(value = "/security/5059/v1/verifyNewUserActivity", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<Boolean> verifyNewUserActivity(@RequestBody ApiCondition condition){
        LOGGER.info("=/api-promotion/coupon/security/5059/v1/verifyNewUserActivity=--开始--{}");

        ResponseResult<Boolean> result = new ResponseResult<>();
        result.setData(couponService.verifyNewUserActivity());
        LOGGER.info("/api-promotion/coupon/security/5059/v1/verifyNewUserActivity结果 result:{}", result);
        return result;
    }


    @ApiOperation(value = "C端用户获新取指定推送优惠券", notes = "C端用户获新指定推送取优惠券")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")
    })
    @RequestMapping(value = "/5063/v1/getSpecifiedPushCoupon", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<List<CouponPushVO>> getSpecifiedPushCoupon(@RequestBody ApiCondition condition) {
        LOGGER.info("=/api-promotion/coupon/5063/v1/getSpecifiedPushCoupon=--开始--{}");

        ResponseResult<List<CouponPushVO>> result = new ResponseResult<>();
        result.setData(couponPushService.getSpecifiedPushCoupon(UserContext.getCurrentCustomerUser()));
        LOGGER.info("/api-promotion/coupon/5063/v1/getSpecifiedPushCoupon结果 result:{}", result);
        return result;
    }

}
