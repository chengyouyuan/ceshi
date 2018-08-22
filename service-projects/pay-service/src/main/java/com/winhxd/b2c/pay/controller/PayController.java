package com.winhxd.b2c.pay.controller;

import com.winhxd.b2c.common.constant.BusinessCode;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.pay.condition.OrderIsPayCondition;
import com.winhxd.b2c.common.domain.pay.condition.PayPreOrderCondition;
import com.winhxd.b2c.common.domain.pay.condition.PayTransfersToWxBankCondition;
import com.winhxd.b2c.common.domain.pay.condition.PayTransfersToWxChangeCondition;
import com.winhxd.b2c.common.domain.pay.vo.OrderPayVO;
import com.winhxd.b2c.common.feign.pay.PayServiceClient;
import com.winhxd.b2c.pay.service.PayService;

import io.swagger.annotations.Api;

@Api(tags = "ApiPay")
@RestController
public class PayController implements PayServiceClient {
	
	@Autowired
	private PayService payService;
	
	/**
	 * @author liuhanning
	 * @date  2018年8月20日 上午11:32:47
	 * @Description 支付
	 * @param condition
	 * @return
	 */
	@Override
	public ResponseResult<OrderPayVO> orderPay(@RequestBody PayPreOrderCondition condition){
		OrderPayVO vo = payService.unifiedOrder(condition);
		ResponseResult<OrderPayVO> result=new ResponseResult<>();
		result.setData(vo);
		return result;
	}
//	/**
//	 * @author liuhanning
//	 * @date  2018年8月20日 上午11:32:47
//	 * @Description 退款
//	 * @param condition
//	 * @return
//	 */
//	@Override
//	public ResponseResult<PayRefundVO> orderRefund(@RequestBody PayRefundCondition condition){
//		PayRefundVO vo=payService.refundOrder(condition);
//		ResponseResult<PayRefundVO> result=new ResponseResult<>();
//		result.setData(vo);
//		return result;
//	}
	@Override
	public ResponseResult<Integer> transfersToChange(@RequestBody PayTransfersToWxChangeCondition condition){
		int data=payService.transfersToChange(condition);
		ResponseResult<Integer> result=new ResponseResult<>();
		result.setData(data);
		return result;
	}
	@Override
	public ResponseResult<Integer> transfersToBank(@RequestBody PayTransfersToWxBankCondition condition){
		int data=payService.transfersToBank(condition);
		ResponseResult<Integer> result=new ResponseResult<>();
		result.setData(data);
		return result;
	}
	/**
	 * @author liuhanning
	 * @date  2018年8月20日 上午11:32:47
	 * @Description 判断订单是否支付过
	 * @param condition
	 * @return
	 */
	@Override
	public ResponseResult<Boolean> orderIsPay(@RequestBody OrderIsPayCondition condition){
		Boolean data=payService.orderIsPay(condition);
		ResponseResult<Boolean> result=new ResponseResult<>();
		result.setData(data);
		return result;
	}

	@Override
	@ApiOperation(value = "轮询确认转账到银行卡记录状态", notes = "轮询确认转账到银行卡记录状态")
	@ApiResponses({@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")})
	public ResponseResult<Integer> confirmTransferToBankStatus() throws Exception {
		ResponseResult<Integer> result=new ResponseResult<>();
		Integer data=payService.confirmTransferToBankStatus();
		result.setData(data);
		return result;
	}
}
