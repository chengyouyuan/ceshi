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
import com.winhxd.b2c.common.domain.pay.vo.OrderPayVO;
import com.winhxd.b2c.pay.service.PayService;
import com.winhxd.b2c.pay.weixin.base.dto.PayRefundDTO;

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
	private ResponseResult<PayRefundDTO> orderRefund(@RequestBody PayRefundCondition condition){
		PayRefundDTO refundOrder=payService.refundOrder(condition);
		ResponseResult<PayRefundDTO> result=new ResponseResult<>();
		result.setData(refundOrder);
		return result;
	}
	@ApiOperation(value = "提现", notes = "提现")
	@ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
		@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
	})
	@PostMapping(value = "/6003/v1/orderWithdrawals", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	private ResponseResult<PayRefundDTO> orderWithdrawals(@RequestBody PayRefundCondition condition){
		PayRefundDTO refundOrder=payService.refundOrder(condition);
		ResponseResult<PayRefundDTO> result=new ResponseResult<>();
		result.setData(refundOrder);
		return result;
	}
}
