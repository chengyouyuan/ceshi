package com.winhxd.b2c.promotion.controller;

import org.springframework.web.bind.annotation.RestController;

import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.feign.promotion.CouponServiceClient;

import io.swagger.annotations.Api;

@Api(tags = "coupon")
@RestController
public class CouponController implements CouponServiceClient{

	@Override
	public ResponseResult<String> getCouponNumsByStore(Long storeId) {
		return null;
	}

}
