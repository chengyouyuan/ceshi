package com.winhxd.b2c.promotion.controller;

import org.springframework.web.bind.annotation.RestController;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.promotion.vo.CouponActivityVO;
import com.winhxd.b2c.common.feign.promotion.CouponServiceClient;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = "coupon")
@RestController
public class CouponController implements CouponServiceClient{

	
	@Override
	@ApiOperation(value = "获取门店用户领取优惠券数量", response = CouponActivityVO.class, notes = "获取门店用户领取优惠券数量")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = String.class),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")})
	public ResponseResult<String> getCouponNumsByCustomerForStore(Long storeId,Long customerId) {
		return null;
	}

}
