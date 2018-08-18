package com.winhxd.b2c.admin.module.pay.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.pay.condition.OrderInfoFinancialInDetailCondition;
import com.winhxd.b2c.common.domain.pay.condition.OrderInfoFinancialOutDetailCondition;
import com.winhxd.b2c.common.domain.pay.vo.OrderInfoFinancialInDetailVO;
import com.winhxd.b2c.common.domain.pay.vo.OrderInfoFinancialOutDetailVO;
import com.winhxd.b2c.common.domain.pay.vo.PayFinanceAccountDetailVO;
import com.winhxd.b2c.common.feign.pay.FinancialManagerServiceClient;

@RestController
@RequestMapping("/pay/financialManager")
public class FinancialManagerController {
	@Autowired
	private FinancialManagerServiceClient managerServiceClient;
	
	/**出入帐汇总查询*/
	/*@PostMapping("/queryStoreFinancialSummary")
	ResponseResult<PayFinanceAccountDetailVO> queryStoreFinancialSummary(){
		return managerServiceClient.queryStoreFinancialSummary();
	}
	
	*//**财务入账明细*//*
	@PostMapping("/queryFinancialInDetail")
	ResponseResult<PagedList<OrderInfoFinancialInDetailVO>> queryFinancialInDetail(@RequestBody OrderInfoFinancialInDetailCondition condition){
		return managerServiceClient.queryFinancialInDetail(condition);
	}
	
	*//**财务出账明细*//*
	@PostMapping("/queryFinancialOutDetail")
	ResponseResult<PagedList<OrderInfoFinancialOutDetailVO>> queryFinancialOutDetail(@RequestBody OrderInfoFinancialOutDetailCondition condition){
		return managerServiceClient.queryFinancialOutDetail(condition);
	}*/

}
