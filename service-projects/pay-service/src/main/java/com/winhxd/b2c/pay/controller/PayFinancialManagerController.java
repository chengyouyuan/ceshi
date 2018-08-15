package com.winhxd.b2c.pay.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.context.StoreUser;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.pay.condition.PayFinanceAccountDetailCondition;
import com.winhxd.b2c.common.domain.pay.vo.PayFinanceAccountDetailVO;
import com.winhxd.b2c.common.feign.pay.FinancialManagerServiceClient;
import com.winhxd.b2c.pay.service.impl.PayFinancialManagerServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(tags = "PayFinancialManager")
public class PayFinancialManagerController implements FinancialManagerServiceClient{
	 private static final Logger logger = LoggerFactory.getLogger(PayFinancialManagerController.class);
	 
	 @Autowired
	 private PayFinancialManagerServiceImpl payFinancialManagerServiceImpl;
	 
	 @Override
	 @ApiOperation(value = "出入帐汇总查询", notes = "出入帐汇总查询")
	 @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_610021, message = "查询结果有误，请联系管理员")})
	 @PostMapping("/pay/61001/v1/queryStoreFinancialSummary")
	 public ResponseResult<PayFinanceAccountDetailVO> queryStoreFinancialSummary() {
        logger.info("/pay/61001/v1/queryStoreFinancialSummary/ 出入帐汇总查询");
        ResponseResult<PayFinanceAccountDetailVO> result = new ResponseResult<PayFinanceAccountDetailVO>();
//        StoreUser currentStoreUser = UserContext.getCurrentStoreUser();
        /// 测试数据//////////////
        StoreUser currentStoreUser = new StoreUser();
        currentStoreUser.setBusinessId(1l);
        ////////////////////////////////
        PayFinanceAccountDetailVO findFinanceAccountDetail = payFinancialManagerServiceImpl.findFinanceAccountDetail(currentStoreUser.getBusinessId());
        result.setData(findFinanceAccountDetail);
        logger.info("/pay/v1/queryStoreFinancialSummary/ 出入帐汇总查询");
        return result;
	 }

	@Override
	@ApiOperation(value = "财务入账明细", notes = "财务入账明细")
	@ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
	       @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")})
	@PostMapping("/pay/61002/v1/queryFinancialInDetail")
	public ResponseResult<PagedList<PayFinanceAccountDetailVO>> queryFinancialInDetail(@RequestBody PayFinanceAccountDetailCondition condition) {
		logger.info("/pay/61002/v1/queryFinancialInDetail 财务入账明细");
		ResponseResult<PagedList<PayFinanceAccountDetailVO>> result = new ResponseResult<PagedList<PayFinanceAccountDetailVO>>();
		PagedList<PayFinanceAccountDetailVO> financialInDetail = payFinancialManagerServiceImpl.findFinancialInDetail(condition);
		result.setData(financialInDetail);
		logger.info("/pay/61002/v1/queryFinancialInDetail 财务入账明细");
		return result;
	}

	@Override
	@ApiOperation(value = "财务出账明细", notes = "财务出账明细")
	@ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
	       @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")})
	@PostMapping("/pay/61003/v1/queryFinancialOutDetail")
	public ResponseResult<PagedList<PayFinanceAccountDetailVO>> queryFinancialOutDetail(@RequestBody PayFinanceAccountDetailCondition condition) {
		logger.info("/pay/61003/v1/queryFinancialOutDetail 财务出账明细");
		ResponseResult<PagedList<PayFinanceAccountDetailVO>> result = new ResponseResult<PagedList<PayFinanceAccountDetailVO>>();
		PagedList<PayFinanceAccountDetailVO> financialOutDetail = payFinancialManagerServiceImpl.findFinancialOutDetail(condition);
		logger.info("财务明细查询的结果数据：----"+financialOutDetail);
		result.setData(financialOutDetail);
		logger.info("/pay/61003/v1/queryFinancialOutDetail 财务出账明细");
		return result;
	}
	 
	 /**公司入账明细*/
	// TODO 待定
}
