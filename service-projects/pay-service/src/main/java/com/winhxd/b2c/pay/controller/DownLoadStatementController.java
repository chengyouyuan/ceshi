package com.winhxd.b2c.pay.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.feign.pay.DownLoadStatementClient;
import com.winhxd.b2c.pay.weixin.service.impl.WXDownloadBillServiceImpl;

@RestController
@Api(tags = "ApiPay")
@RequestMapping(value = "/download")
public class DownLoadStatementController implements DownLoadStatementClient {

	private static final Logger logger = LoggerFactory.getLogger(DownLoadStatementController.class);
	 
	@Autowired
	private WXDownloadBillServiceImpl wXDownloadBillServiceImpl;
	
	@Override
	@ApiOperation(value = "下载对账单", notes = "下载对账单")
	@ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
	       @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")})
	public ResponseResult<String> downloadStatement() {
		logger.info("/download/6155/v1/downloadStatement 下载对账单");
		ResponseResult<String> result = new ResponseResult<String>();
		String res = wXDownloadBillServiceImpl.downloadStatement();
		result.setData(res);
		return result;
	}
	
	@Override
	@ApiOperation(value = "下载资金账单", notes = "下载资金账单")
	@ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
		@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")})
	public ResponseResult<String> downloadFundFlow() {
		logger.info("/download/6156/v1/downloadFundFlow 下载资金账单");
		ResponseResult<String> result = new ResponseResult<String>();
		String res = wXDownloadBillServiceImpl.downloadFundFlow();
		result.setData(res);
		return result;
	}
	
	
	
}
