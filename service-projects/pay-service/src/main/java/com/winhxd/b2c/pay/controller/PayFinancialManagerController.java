package com.winhxd.b2c.pay.controller;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.pay.condition.FinancialManagerCondition;
import com.winhxd.b2c.common.domain.pay.vo.PayFinanceAccountDetailVO;
import com.winhxd.b2c.common.feign.pay.FinancialManagerServiceClient;
import com.winhxd.b2c.pay.service.PayFinancialManagerService;
import com.winhxd.b2c.pay.service.impl.PayFinancialManagerServiceImpl;
import com.winhxd.b2c.pay.weixin.service.WXDownloadBillService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "PayFinancialManager")
public class PayFinancialManagerController implements FinancialManagerServiceClient{
	 private static final Logger logger = LoggerFactory.getLogger(PayFinancialManagerController.class);
	 
	 @Autowired
	 private PayFinancialManagerServiceImpl payFinancialManagerServiceImpl;
	 
	 @Autowired
	 private WXDownloadBillService WXDownloadBillServiceImpl;
	@Autowired
	private PayFinancialManagerService payFinancialManagerService;
	 
	 @Override
	 @ApiOperation(value = "出入帐汇总查询", notes = "出入帐汇总查询")
	 @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
			 @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")})
	 public ResponseResult<PayFinanceAccountDetailVO> queryStoreFinancialSummary(@RequestBody FinancialManagerCondition condition) {
        logger.info("/pay/6101/v1/queryStoreFinancialSummary/ 出入帐汇总查询");
        ResponseResult<PayFinanceAccountDetailVO> result = new ResponseResult<PayFinanceAccountDetailVO>();
        PayFinanceAccountDetailVO findFinanceAccountDetail = payFinancialManagerService.findStoreFinancialSummary();
        //StoreUser currentStoreUser = UserContext.getCurrentStoreUser();
        /// 测试数据//////////////
//        StoreUser currentStoreUser = new StoreUser();
//        currentStoreUser.setBusinessId(1l);
        //////////////////////////////
        //PayFinanceAccountDetailVO findFinanceAccountDetail = payFinancialManagerServiceImpl.findFinanceAccountDetail(currentStoreUser.getBusinessId());
        result.setData(findFinanceAccountDetail);
        logger.info("/pay/6101/v1/queryStoreFinancialSummary/ 出入帐汇总查询");
        return result;
	 }

	/*@Override
	@ApiOperation(value = "财务入账明细", notes = "财务入账明细")
	@ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
	       @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")})
//	@PostMapping("/pay/6102/v1/queryFinancialInDetail")
	public ResponseResult<PagedList<OrderInfoFinancialInDetailVO>> queryFinancialInDetail(@RequestBody OrderInfoFinancialInDetailCondition condition) {
		logger.info("/pay/6102/v1/queryFinancialInDetail 财务入账明细");
		ResponseResult<PagedList<OrderInfoFinancialInDetailVO>> result = new ResponseResult<PagedList<OrderInfoFinancialInDetailVO>>();
		PagedList<OrderInfoFinancialInDetailVO> financialInDetail = payFinancialManagerServiceImpl.queryFinancialInDetail(condition);
		result.setData(financialInDetail);
		logger.info("/pay/6102/v1/queryFinancialInDetail 财务入账明细");
		return result;
	}

	@Override
	@ApiOperation(value = "财务出账明细", notes = "财务出账明细")
	@ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
	       @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")})
//	@PostMapping("/pay/6103/v1/queryFinancialOutDetail")
	public ResponseResult<PagedList<OrderInfoFinancialOutDetailVO>> queryFinancialOutDetail(@RequestBody OrderInfoFinancialOutDetailCondition condition) {
		logger.info("/pay/6103/v1/queryFinancialOutDetail 财务出账明细");
		ResponseResult<PagedList<OrderInfoFinancialOutDetailVO>> result = new ResponseResult<PagedList<OrderInfoFinancialOutDetailVO>>();
		PagedList<OrderInfoFinancialOutDetailVO> financialOutDetail = payFinancialManagerServiceImpl.queryFinancialOutDetail(condition);
		logger.info("财务明细查询的结果数据：----"+financialOutDetail);
		result.setData(financialOutDetail);
		logger.info("/pay/6103/v1/queryFinancialOutDetail 财务出账明细");
		return result;
	}
	 */
	 /**公司入账明细*/
	// TODO 待定

	@ApiOperation(value = "下载对账单", notes = "下载对账单")
	@ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
	       @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")})
	@PostMapping("/pay/6155/v1/downloadBill")
	public ResponseResult<PagedList<PayFinanceAccountDetailVO>> downloadBill() {
		logger.info("/pay/6155/v1/downloadBill 下载对账单");
		WXDownloadBillServiceImpl.downloadStatement();
		return null;
	}
}
