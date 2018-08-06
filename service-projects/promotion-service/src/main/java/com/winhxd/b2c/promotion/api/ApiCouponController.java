package com.winhxd.b2c.promotion.api;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.promotion.condition.CouponCondition;
import com.winhxd.b2c.common.domain.promotion.vo.CouponVO;
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
public class ApiCouponController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiCouponController.class);

    @ApiOperation(value = "新人专享优惠列表", response = Boolean.class, notes = "新人专享优惠列表")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = Boolean.class),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")
    })
    @RequestMapping(value = "/501/v1/getNewUserCouponList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<List<CouponVO>> handleOrderRefundByStore(@RequestBody CouponCondition couponCondition) {
        LOGGER.info("=/api-coupon/coupon/501/v1/getNewUserCouponList-查询新人专享优惠列表=--开始--{}", couponCondition);
        ResponseResult<CouponVO> result = new ResponseResult<>();
        try {
            //返回对象
        } catch (Exception e) {
            LOGGER.error("=/api-coupon/coupon/501/v1/getNewUserCouponList-查询新人专享优惠列表=--异常" + e.getMessage(), e);
            result.setCode(BusinessCode.CODE_1001);
        }
        LOGGER.info("=/api-coupon/coupon/501/v1/getNewUserCouponList-查询新人专享优惠列表=--结束 result={}", result);
        return null;
    }
}
