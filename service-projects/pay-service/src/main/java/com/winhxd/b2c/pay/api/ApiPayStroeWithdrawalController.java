package com.winhxd.b2c.pay.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.pay.condition.PayStoreApplyWithDrawCondition;
import com.winhxd.b2c.common.domain.pay.vo.PayWithdrawalPageVO;
import com.winhxd.b2c.pay.service.impl.PayStoreWithdrawalServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(tags = "ApiPay")
@RequestMapping(value = "/api-pay/withdrawal")
public class ApiPayStroeWithdrawalController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ApiPayStroeWithdrawalController.class);
	
	@Autowired
	private PayStoreWithdrawalServiceImpl payStoreWithdrawalService;
	
	
	@ApiOperation(value = "门店提现到微信或者银行卡", notes = "门店提现到微信或者银行卡")
	@ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
		@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
		@ApiResponse(code = BusinessCode.CODE_610022, message = "请传入提现类型参数")
	})
	@PostMapping(value = "/6107/v1/toWithdrawalPage", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	private ResponseResult<PayWithdrawalPageVO> toPayStoreWithdrawalPage(@RequestBody PayStoreApplyWithDrawCondition condition){
		ResponseResult<PayWithdrawalPageVO> result = new ResponseResult<PayWithdrawalPageVO>();
		if(condition.getWithdrawType() != 0){
			result = payStoreWithdrawalService.showPayWithdrawalDetail(condition);
		}else{
			result.setCode(BusinessCode.CODE_610022);
			LOGGER.info("请传入提现类型参数");
		}
		return result;
	}
	
	@ApiOperation(value = "门店提现到微信或者银行卡", notes = "门店提现到微信或者银行卡")
	@ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
		@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
		@ApiResponse(code = BusinessCode.CODE_610022, message = "请传入提现类型参数")
	})
	@PostMapping(value = "/6108/v1/withdrawal", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	private ResponseResult<Integer> payStoreWithdrawal(@RequestBody PayStoreApplyWithDrawCondition condition){
		ResponseResult<Integer> result = new ResponseResult<Integer>();
		if(condition.getWithdrawType() != 0){
			payStoreWithdrawalService.saveStorWithdrawalInfo(condition);
		}else{
			result.setCode(BusinessCode.CODE_610022);
			LOGGER.info("请传入提现类型");
		}
		return result;
	}
	
	
	
}
