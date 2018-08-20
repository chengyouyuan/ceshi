package com.winhxd.b2c.pay.api;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.pay.condition.OrderPayCondition;
import com.winhxd.b2c.common.domain.pay.enums.BanksEnums;
import com.winhxd.b2c.common.domain.pay.vo.BanksVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(tags = "ApiPay")
@RequestMapping(value = "/api-pay/pay")
public class ApiPayController {

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
}
