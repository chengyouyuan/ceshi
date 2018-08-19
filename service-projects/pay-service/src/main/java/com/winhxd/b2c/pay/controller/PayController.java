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
import com.winhxd.b2c.common.domain.pay.vo.PayPreOrderVO;
import com.winhxd.b2c.common.domain.pay.vo.PayRefundVO;
import com.winhxd.b2c.common.feign.pay.PayServiceClient;
import com.winhxd.b2c.pay.service.PayService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(tags = "ApiPay")
@RequestMapping(value = "/pay")
public class PayController implements PayServiceClient {
	
	@Autowired
	private PayService payService;
	
	@Override
	@ApiOperation(value = "订单支付", notes = "订单支付")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
    })
	public ResponseResult<PayPreOrderVO> orderPay(@RequestBody PayPreOrderCondition condition){
		PayPreOrderVO vo = payService.unifiedOrder(condition);
		ResponseResult<PayPreOrderVO> result=new ResponseResult<>();
		result.setData(vo);
		return result;
	}
	@Override
	@ApiOperation(value = "退款", notes = "退款")
	@ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
		@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
	})
	@PostMapping(value = "/6002/v1/orderRefund", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseResult<PayRefundVO> orderRefund(@RequestBody PayRefundCondition condition){
		PayRefundVO vo=payService.refundOrder(condition);
		ResponseResult<PayRefundVO> result=new ResponseResult<>();
		result.setData(vo);
		return result;
	}
	@Override
	@ApiOperation(value = "提现到微信钱包", notes = "提现到微信钱包")
	@ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
		@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
	})
	public ResponseResult<Integer> transfersToChange(@RequestBody PayTransfersToWxChangeCondition condition){
		int data=payService.transfersToChange(condition);
		ResponseResult<Integer> result=new ResponseResult<>();
		result.setData(data);
		return result;
	}
	@Override
	@ApiOperation(value = "提现到微信银行卡", notes = "提现到微信银行卡")
	@ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
		@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
	})
	public ResponseResult<Integer> transfersToBank(@RequestBody PayTransfersToWxBankCondition condition){
		int data=payService.transfersToBank(condition);
		ResponseResult<Integer> result=new ResponseResult<>();
		result.setData(data);
		return result;
	}
}
