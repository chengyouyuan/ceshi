package com.winhxd.b2c.pay.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.pay.condition.PayPreOrderCondition;
import com.winhxd.b2c.common.domain.pay.vo.OrderPayVO;
import com.winhxd.b2c.pay.service.PayService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(tags = "ApiPay")
@RequestMapping(value = "/pay")
public class PayController {
	
	@Autowired
	private PayService payService;
	
	@ApiOperation(value = "订单支付", notes = "订单支付")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
    })
	@PostMapping(value = "/6001/v1/orderPay", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	private ResponseResult<OrderPayVO> orderPay(@RequestBody PayPreOrderCondition condition){
		ResponseResult<OrderPayVO> result=payService.orderPay(condition);
		return result;
	}
}
