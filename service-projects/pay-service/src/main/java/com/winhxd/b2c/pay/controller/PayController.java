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
import com.winhxd.b2c.common.domain.pay.condition.PayRefundCondition;
import com.winhxd.b2c.common.domain.pay.condition.PayTransfersToWxBankCondition;
import com.winhxd.b2c.common.domain.pay.condition.PayTransfersToWxChangeCondition;
import com.winhxd.b2c.common.domain.pay.vo.OrderPayVO;
import com.winhxd.b2c.common.domain.pay.vo.PayRefundVO;
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
		OrderPayVO vo=payService.unifiedOrder(condition);
		ResponseResult<OrderPayVO> result=new ResponseResult<>();
		result.setData(vo);
		return result;
	}
	@ApiOperation(value = "退款", notes = "退款")
	@ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
		@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
	})
	@PostMapping(value = "/6002/v1/orderRefund", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	private ResponseResult<PayRefundVO> orderRefund(@RequestBody PayRefundCondition condition){
		PayRefundVO vo=payService.refundOrder(condition);
		ResponseResult<PayRefundVO> result=new ResponseResult<>();
		result.setData(vo);
		return result;
	}
	@ApiOperation(value = "提现到微信钱包", notes = "提现到微信钱包")
	@ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
		@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
	})
	@PostMapping(value = "/6003/v1/transfersToChange", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	private ResponseResult<String> transfersToChange(@RequestBody PayTransfersToWxChangeCondition condition){
		String data=payService.transfersToChange(condition);
		ResponseResult<String> result=new ResponseResult<>();
		result.setData(data);
		return result;
	}
	@ApiOperation(value = "提现到微信银行卡", notes = "提现到微信银行卡")
	@ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
		@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
	})
	@PostMapping(value = "/6004/v1/transfersToBank", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	private ResponseResult<String> transfersToBank(@RequestBody PayTransfersToWxBankCondition condition){
		String data=payService.transfersToBank(condition);
		ResponseResult<String> result=new ResponseResult<>();
		result.setData(data);
		return result;
	}
}
