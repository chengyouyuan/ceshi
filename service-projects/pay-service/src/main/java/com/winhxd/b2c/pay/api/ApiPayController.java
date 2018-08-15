package com.winhxd.b2c.pay.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.pay.condition.OrderPayCondition;
import com.winhxd.b2c.common.domain.pay.condition.OrderRefundCondition;
import com.winhxd.b2c.common.domain.pay.enums.BanksEnums;
import com.winhxd.b2c.common.domain.pay.vo.BanksVO;
import com.winhxd.b2c.common.domain.pay.vo.OrderRefundVO;
import com.winhxd.b2c.pay.service.PayService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(tags = "ApiPay")
@RequestMapping(value = "/api-pay/pay")
public class ApiPayController {

	@Autowired
	private PayService payService;
	
	
	@ApiOperation(value = "退款", notes = "退款")
	@ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
		@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
	})
	@PostMapping(value = "/6002/v1/orderRefund", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	private ResponseResult<OrderRefundVO> orderRefund(@RequestBody OrderRefundCondition condition){
		ResponseResult<OrderRefundVO> result=payService.orderRefund(condition);
		return result;
	}
	@ApiOperation(value = "获取支付凭证", notes = "获取支付凭证")
	@ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
		@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
	})
	@PostMapping(value = "/6003/v1/getprepayId", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	private ResponseResult<String> getprepayId(@RequestBody OrderPayCondition condition){
		ResponseResult<String> result=payService.getprepayId(condition);
		
		return result;
	}
	@ApiOperation(value = "获取转账银行列表", notes = "获取转账银行列表")
	@ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
		@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
	})
	@PostMapping(value = "/6004/v1/getBanks", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	private ResponseResult<List<BanksVO>> getBanks(@RequestBody OrderPayCondition condition){
		ResponseResult<List<BanksVO>> result=new ResponseResult<>();
		result.setData(BanksEnums.getValus());
		return result;
	}
	
}
