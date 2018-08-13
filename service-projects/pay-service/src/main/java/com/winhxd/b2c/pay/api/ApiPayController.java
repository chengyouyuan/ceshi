package com.winhxd.b2c.pay.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.pay.condition.OrderPayCondition;
import com.winhxd.b2c.common.domain.pay.vo.OrderPayVO;
import com.winhxd.b2c.common.domain.pay.vo.RefundVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(tags = "ApiPay")
@RequestMapping(value = "/api-pay/pay")
public class ApiPayController {

	 
	@ApiOperation(value = "订单支付", notes = "订单支付")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效"),
            @ApiResponse(code = BusinessCode.ORDER_NO_EMPTY, message = "订单号为空"),
            @ApiResponse(code = BusinessCode.WRONG_ORDERNO, message = "订单号错误"),
            @ApiResponse(code = BusinessCode.WRONG_ORDER_STATUS, message = "订单状态错误"),
    })
	@PostMapping(value = "/601/v1/orderPay", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	private ResponseResult<OrderPayVO> orderPay(@RequestBody OrderPayCondition condition){
		ResponseResult<OrderPayVO> result=new ResponseResult<>();
		return result;
	}
	@ApiOperation(value = "退款", notes = "退款")
	@ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
		@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
		@ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效"),
		@ApiResponse(code = BusinessCode.ORDER_NO_EMPTY, message = "订单号为空"),
		@ApiResponse(code = BusinessCode.WRONG_ORDERNO, message = "订单号错误"),
		@ApiResponse(code = BusinessCode.WRONG_ORDER_STATUS, message = "订单状态错误"),
	})
	@PostMapping(value = "/602/v1/refund", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	private ResponseResult<RefundVO> refund(@RequestBody OrderPayCondition condition){
		ResponseResult<RefundVO> result=new ResponseResult<>();
		return result;
	}
	@ApiOperation(value = "审核退款", notes = "审核退款")
	@ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
		@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
		@ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效"),
		@ApiResponse(code = BusinessCode.ORDER_NO_EMPTY, message = "订单号为空"),
		@ApiResponse(code = BusinessCode.WRONG_ORDERNO, message = "订单号错误"),
		@ApiResponse(code = BusinessCode.WRONG_ORDER_STATUS, message = "订单状态错误"),
	})
	@PostMapping(value = "/603/v1/auditRefund", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	private ResponseResult<OrderPayVO> auditRefund(@RequestBody OrderPayCondition condition){
		ResponseResult<OrderPayVO> result=new ResponseResult<>();
		return result;
	}
	@ApiOperation(value = "获取支付凭证", notes = "获取支付凭证")
	@ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
		@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
		@ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效"),
	})
	@PostMapping(value = "/604/v1/getprepayId", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	private ResponseResult<OrderPayVO> getprepayId(@RequestBody OrderPayCondition condition){
		ResponseResult<OrderPayVO> result=new ResponseResult<>();
		return result;
	}
	
}
