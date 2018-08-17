package com.winhxd.b2c.pay.api;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.pay.condition.OrderPayCondition;
import com.winhxd.b2c.common.domain.pay.condition.StoreBindStoreWalletCondition;
import com.winhxd.b2c.common.domain.pay.enums.BanksEnums;
import com.winhxd.b2c.common.domain.pay.model.PayStoreWallet;
import com.winhxd.b2c.common.domain.pay.vo.BanksVO;
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
	
	@ApiOperation(value = "获取转账银行列表", notes = "获取转账银行列表")
	@ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
		@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
	})
	@PostMapping(value = "/6005/v1/getBanks", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	private ResponseResult<List<BanksVO>> getBanks(@RequestBody OrderPayCondition condition){
		ResponseResult<List<BanksVO>> result=new ResponseResult<>();
		result.setData(BanksEnums.getValus());
		return result;
	}
	@ApiOperation(value = "获取转账钱包", notes = "获取转账钱包")
	@ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
		@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
	})
	@PostMapping(value = "/6006/v1/getPayStoreWallet", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	private ResponseResult<PayStoreWallet> getPayStoreWallet(@RequestBody OrderPayCondition condition){
		ResponseResult<PayStoreWallet> result=new ResponseResult<>();
		List<PayStoreWallet> walletList=payService.selectPayStoreWalletByStoreId();
		PayStoreWallet wallet=null;
		if (CollectionUtils.isNotEmpty(walletList)) {
			wallet=walletList.get(0);
		}
		result.setData(wallet);
		return result;
	}
	@ApiOperation(value = "门店绑定转账钱包", notes = "门店绑定转账钱包")
	@ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
		@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
	})
	@PostMapping(value = "/6007/v1/storeBindStoreWallet", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	private ResponseResult<Void> storeBindStoreWallet(@RequestBody StoreBindStoreWalletCondition condition){
		ResponseResult<Void> result=new ResponseResult<>();
		payService.storeBindStoreWallet(condition);
		return result;
	}
	
	
	
}
