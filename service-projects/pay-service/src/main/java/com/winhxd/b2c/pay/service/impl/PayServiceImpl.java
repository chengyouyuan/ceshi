package com.winhxd.b2c.pay.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.order.condition.OrderRefundCallbackCondition;
import com.winhxd.b2c.common.domain.pay.condition.OrderPayCondition;
import com.winhxd.b2c.common.domain.pay.condition.OrderRefundCondition;
import com.winhxd.b2c.common.domain.pay.condition.UpdateOrderCondition;
import com.winhxd.b2c.common.domain.pay.model.PayOrderPayment;
import com.winhxd.b2c.common.domain.pay.vo.OrderPayVO;
import com.winhxd.b2c.common.domain.pay.vo.OrderRefundVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.order.OrderServiceClient;
import com.winhxd.b2c.pay.dao.PayOrderPaymentMapper;
import com.winhxd.b2c.pay.service.PayService;

@Service
public class PayServiceImpl implements PayService{
	
	private static final Logger logger = LoggerFactory.getLogger(PayServiceImpl.class);
	@Autowired
	OrderServiceClient orderServiceClient;
	
	@Autowired
	PayOrderPaymentMapper payOrderPaymentMapper;
	
	private static final String logLabel="PayServiceImpl--";
	@Override
	public ResponseResult<OrderRefundVO> orderRefund(OrderRefundCondition condition) {
		//todo 调取微信退款接口  
		
		return null;
	}

	@Override
	public ResponseResult<String> getprepayId(OrderPayCondition condition) {
		//todo 调取微信的接口
		return null;
	}

	@Override
	public ResponseResult<OrderPayVO> orderPay(OrderPayCondition condition) {
		//todo 调取微信支付接口  
		return null;
	}

	@Override
	public Integer callbackOrderPay(UpdateOrderCondition condition) {
		
		//todo 调用订单接口
		
		//todo 插入流水号
		
		//todo插入公司预入账数据（对账的时候更新为实入账）
		//todo给用户插入待结算金额和总的收入金额
		return null;
	}


	@Override
	@Transactional
	public Integer callbackOrderRefund(UpdateOrderCondition condition) {
		logger.info(logLabel+"退款回调callbackOrderRefund--开始");
		logger.info(logLabel+"退款回调callbackOrderRefund--参数"+condition.toString());
		//更新订单状态
		OrderRefundCallbackCondition orderRefundCallbackCondition=new OrderRefundCallbackCondition();
		ResponseResult<Boolean> callbackResult=orderServiceClient.updateOrderRefundCallback(orderRefundCallbackCondition);
		if (callbackResult.getCode()!=0&&!callbackResult.getData()) {
			//订单更新失败
			logger.info(logLabel+"退款回调callbackOrderRefund--订单更新失败");
			throw new BusinessException(BusinessCode.CODE_600301);
		}
		PayOrderPayment payOrderPayment=new PayOrderPayment();
		int insertResult=payOrderPaymentMapper.insertSelective(payOrderPayment);
		if (insertResult<1) {
			//订单更新失败
			logger.info(logLabel+"退款回调callbackOrderRefund--订单支付流水插入失败");
			throw new BusinessException(BusinessCode.CODE_600301);
		}

		
		//todo 将此订单的预入账记录更新为退款记录
		logger.info(logLabel+"退款回调callbackOrderRefund--结束");
		return null;
	}
}
