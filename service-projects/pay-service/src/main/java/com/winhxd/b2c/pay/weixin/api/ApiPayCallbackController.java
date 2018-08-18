package com.winhxd.b2c.pay.weixin.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.pay.condition.OrderPayCondition;
import com.winhxd.b2c.pay.weixin.base.config.PayConfig;
import com.winhxd.b2c.pay.weixin.model.PayBill;
import com.winhxd.b2c.pay.weixin.model.PayRefund;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 微信支付回调
 * @author mahongliang
 * @date  2018年8月18日 下午8:43:21
 * @Description 
 * @version
 */
@Controller
@Api(tags = "Api CallBack")
public class ApiPayCallbackController {
	
/*	@ApiOperation(value = "微信支付回调", notes = "微信支付回调")
	@PostMapping(value = "${WX.PAY_NOTIFY_URL}")
	private void unifiedOrderCallback(HttpServletRequest request,HttpServletResponse response){
		return ;
	}
	
	@ApiOperation(value = "微信退款回调", notes = "微信退款回调")
	@ApiResponses({
		@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
		@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")
	})
	@PostMapping(value = "${WX.REFUND_NOTIFY_URL}")
	private void refundCallback(HttpServletRequest request,HttpServletResponse response){
		return;
	}*/

}
