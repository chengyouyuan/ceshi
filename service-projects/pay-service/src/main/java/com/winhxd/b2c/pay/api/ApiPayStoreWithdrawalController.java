package com.winhxd.b2c.pay.api;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.pay.condition.PayCondition;
import com.winhxd.b2c.common.domain.pay.condition.PayStoreApplyWithDrawCondition;
import com.winhxd.b2c.common.domain.pay.model.PayWithdrawalsType;
import com.winhxd.b2c.common.domain.pay.vo.PayWithdrawalPageVO;
import com.winhxd.b2c.common.domain.pay.vo.PayWithdrawalsTypeVO;
import com.winhxd.b2c.pay.service.impl.PayStoreWithdrawalServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(tags = "ApiPay")
@RequestMapping(value = "/api-pay/withdrawal")
public class ApiPayStoreWithdrawalController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ApiPayStoreWithdrawalController.class);
	
	@Autowired
	private PayStoreWithdrawalServiceImpl payStoreWithdrawalService;
	
	@ApiOperation(value = "返回提现类型", notes = "返回提现类型")
	@ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
		@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常") 
	})
	@PostMapping(value = "/6107/v1/getWithdrawalType", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	private ResponseResult<List<PayWithdrawalsTypeVO>> getStoreWithdrawalType(@RequestBody PayCondition condition){
		ResponseResult<List<PayWithdrawalsTypeVO>> result = new ResponseResult<List<PayWithdrawalsTypeVO>>();
		ResponseResult<List<PayWithdrawalsType>> allWithdrawalType = payStoreWithdrawalService.getAllWithdrawalType();
		List<PayWithdrawalsTypeVO> listvo = new ArrayList<PayWithdrawalsTypeVO>();
		for(PayWithdrawalsType type:allWithdrawalType.getData()){
			PayWithdrawalsTypeVO typevo = new PayWithdrawalsTypeVO();
			BeanUtils.copyProperties(type, typevo);
			listvo.add(typevo);
		}
		result.setData(listvo);
		result.setCode(allWithdrawalType.getCode());
		return result;
	}
	
	@ApiOperation(value = "门店进入提现页面", notes = "门店进入提现页面")
	@ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
		@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
		@ApiResponse(code = BusinessCode.CODE_610022, message = "请传入提现类型参数"),
		@ApiResponse(code = BusinessCode.CODE_610025, message = "请先绑定银行卡"),
		@ApiResponse(code = BusinessCode.CODE_610026, message = "请先绑定微信账号")
		
	})
	@PostMapping(value = "/6108/v1/toWithdrawalPage", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	private ResponseResult<PayWithdrawalPageVO> toPayStoreWithdrawalPage(@RequestBody PayStoreApplyWithDrawCondition condition){
		ResponseResult<PayWithdrawalPageVO> result = new ResponseResult<PayWithdrawalPageVO>();
		if(condition.getWithdrawType() != 0){
			LOGGER.info("/6108/v1/toWithdrawalPage-门店进入提现页面入参："+condition);
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
	@PostMapping(value = "/6109/v1/withdrawal", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	private ResponseResult<Integer> payStoreWithdrawal(@RequestBody PayStoreApplyWithDrawCondition condition){
		LOGGER.info("/6109/v1/withdrawal-门店提现到微信或者银行卡："+condition);
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
