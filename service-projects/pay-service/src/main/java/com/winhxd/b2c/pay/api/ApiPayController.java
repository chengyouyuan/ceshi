package com.winhxd.b2c.pay.api;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.pay.condition.DownloadStatementCondition;
import com.winhxd.b2c.common.domain.pay.condition.OrderPayCondition;
import com.winhxd.b2c.common.domain.pay.enums.BanksEnums;
import com.winhxd.b2c.common.domain.pay.vo.BanksVO;
import com.winhxd.b2c.common.util.JsonUtil;
import com.winhxd.b2c.pay.weixin.service.WXDownloadBillService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(tags = "ApiPay")
@RequestMapping(value = "/api-pay/pay")
public class ApiPayController {

	private static final Logger logger = LoggerFactory.getLogger(ApiPayController.class);
	@Autowired
	private WXDownloadBillService wxDownloadBillService;


	@ApiOperation(value = "获取转账银行列表", notes = "获取转账银行列表")
	@ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
		@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
	})
	@PostMapping(value = "/6005/v1/getBanks", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	private ResponseResult<PagedList<BanksVO>> getBanks(@RequestBody OrderPayCondition condition){
		ResponseResult<PagedList<BanksVO>> result=new ResponseResult<>();
		PagedList<BanksVO> pageVo = new PagedList<BanksVO>();
		List<BanksVO> valus = BanksEnums.getValus();
		pageVo.setData(valus);
		result.setData(pageVo);
		return result;
	}

	@ApiOperation(value = "手动下载对账单", notes = "手动下载对账单")
	@PostMapping(value = "/security/6161/v1/downloadStatements", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
			@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
	})
	ResponseResult<String> downloadStatement(@RequestBody DownloadStatementCondition condition) {
		logger.info("/security/6161/v1/downloadStatements 接口调用开始，对账单下载日期为--{}", JsonUtil.toJSONString(condition));
		ResponseResult<String> responseResult = new ResponseResult<>();
		condition.setAccountType(DownloadStatementCondition.SourceType.BASIC.getText());
		condition.setStatementType(DownloadStatementCondition.StatementType.ALL.getText());
		//下载对账单
		String res = wxDownloadBillService.downloadStatement(condition);
		//下载资金账单
		String res1 = wxDownloadBillService.downloadFundFlow(condition);
		responseResult.setData(res);
		return responseResult;
	}
}
