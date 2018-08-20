package com.winhxd.b2c.pay.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.pay.condition.DownloadStatementCondition;
import com.winhxd.b2c.common.domain.pay.model.PayStatementDownloadRecord;
import com.winhxd.b2c.common.feign.pay.DownLoadStatementClient;
import com.winhxd.b2c.pay.weixin.base.dto.PayFinancialBillDTO;
import com.winhxd.b2c.pay.weixin.base.dto.PayStatementDTO;
import com.winhxd.b2c.pay.weixin.service.WXDownloadBillService;

@RestController
@Api(tags = "ApiPay")
@RequestMapping(value = "")
public class DownLoadStatementController implements DownLoadStatementClient {

	private static final Logger logger = LoggerFactory.getLogger(DownLoadStatementController.class);
	 
	@Autowired
	private WXDownloadBillService wXDownloadBillService;
	
	@Override
	@ApiOperation(value = "下载对账单", notes = "下载对账单")
	@ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
	       @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")})
	public ResponseResult<String> downloadStatement(DownloadStatementCondition condition) {
		logger.info("/6155/v1/downloadStatement 下载对账单");
		ResponseResult<String> result = new ResponseResult<String>();
		condition.setStatementType(PayStatementDTO.StatementType.ALL.getText());
		String res = wXDownloadBillService.downloadStatement(condition);
		result.setData(res);
		return result;
	}
	
	@Override
	@ApiOperation(value = "下载资金账单", notes = "下载资金账单")
	@ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
		@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")})
	public ResponseResult<String> downloadFundFlow(DownloadStatementCondition condition) {
		logger.info("/6156/v1/downloadFundFlow 下载资金账单");
		ResponseResult<String> result = new ResponseResult<String>();
		condition.setAccountType(PayFinancialBillDTO.SourceType.OPERATION.getText());
		String res = wXDownloadBillService.downloadFundFlow(condition);
		result.setData(res);
		return result;
	}
	
	@Override
	@ApiOperation(value = "查询账单记录表", notes = "查询账单记录表")
	@ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
		@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")})
	public ResponseResult<List<PayStatementDownloadRecord>> findDownloadRecord(@RequestBody PayStatementDownloadRecord record) {
		logger.info("/6157/v1/findDownloadRecord 查询账单记录表");
		ResponseResult<List<PayStatementDownloadRecord>> result = new ResponseResult<>();
		List<PayStatementDownloadRecord> list = wXDownloadBillService.findDownloadRecord(record);
		result.setData(list);
		return result;
	}
	
	
	
}
