package com.winhxd.b2c.pay.controller;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.order.model.OrderInfo;
import com.winhxd.b2c.common.domain.pay.condition.*;
import com.winhxd.b2c.common.domain.pay.vo.OrderPayVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.pay.PayServiceClient;
import com.winhxd.b2c.pay.service.PayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@Api(tags = "ApiPay")
@RestController
public class PayController implements PayServiceClient {

	private static final Logger logger = LoggerFactory.getLogger(PayController.class);

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
		String log = "订单支付orderPay";
		logger.info(log + "支付的参数为---{}", condition.toString());
		if (condition == null) {
			logger.info(log + "--参数为空");
			throw new BusinessException(BusinessCode.CODE_600101);
		}
		String orderNo = condition.getOutOrderNo();
		String spbillCreateIp = condition.getSpbillCreateIp();
		String body = condition.getBody();
		String openid = condition.getOpenid();
		Short payType = condition.getPayType();
		BigDecimal totalAmount = condition.getTotalAmount();
		if (StringUtils.isBlank(orderNo)) {
			logger.info(log + "--订单号为空");
			throw new BusinessException(BusinessCode.CODE_600102);
		}
		log += "订单号为--：" + orderNo;
		if (StringUtils.isBlank(body)) {
			logger.info(log + "--商品描述为空");
			throw new BusinessException(BusinessCode.CODE_600103);
		}

		if (StringUtils.isBlank(openid)) {
			logger.info(log + "--用户openid为空");
			throw new BusinessException(BusinessCode.CODE_600104);
		}
		if (StringUtils.isBlank(spbillCreateIp)) {
			logger.info(log + "--设备ip为空");
			throw new BusinessException(BusinessCode.CODE_600105);
		}
		if (payType == null) {
			logger.info(log + "--支付方式为空");
			throw new BusinessException(BusinessCode.CODE_600106);
		}
		if (totalAmount == null) {
			logger.info(log + "--支付金额为空");
			throw new BusinessException(BusinessCode.CODE_600106);
		}
		OrderPayVO vo = payService.unifiedOrder(condition);
		ResponseResult<OrderPayVO> result=new ResponseResult<>();
		result.setData(vo);
		return result;
	}
	/**
	 * @author liuhanning
	 * @date  2018年8月20日 上午11:32:47
	 * @Description 退款
	 * @param condition
	 * @return
	 */
	@Override
	public ResponseResult<Void> orderRefund(@RequestBody PayRefundCondition condition){
		String orderNo = condition.getOrderNo();
		OrderInfo order = new OrderInfo();
		order.setPaymentSerialNum(condition.getPaymentSerialNum());
		order.setUpdatedBy(condition.getUpdatedBy());
		order.setUpdatedByName(condition.getUpdatedByName());
		order.setCancelReason(condition.getCancelReason());

		payService.refundOrder(orderNo,order);
		ResponseResult<Void> result=new ResponseResult<>();
		return result;
	}
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
