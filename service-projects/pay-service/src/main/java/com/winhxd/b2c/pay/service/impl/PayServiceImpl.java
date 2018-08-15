package com.winhxd.b2c.pay.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.order.condition.OrderRefundCallbackCondition;
import com.winhxd.b2c.common.domain.pay.condition.OrderPayCallbackCondition;
import com.winhxd.b2c.common.domain.pay.condition.OrderPayCondition;
import com.winhxd.b2c.common.domain.pay.condition.OrderRefundCondition;
import com.winhxd.b2c.common.domain.pay.condition.StoreBankrollChangeCondition;
import com.winhxd.b2c.common.domain.pay.condition.UpdateOrderCondition;
import com.winhxd.b2c.common.domain.pay.model.PayOrderPayment;
import com.winhxd.b2c.common.domain.pay.model.StoreBankroll;
import com.winhxd.b2c.common.domain.pay.vo.OrderPayVO;
import com.winhxd.b2c.common.domain.pay.vo.OrderRefundVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.order.OrderServiceClient;
import com.winhxd.b2c.pay.dao.PayOrderPaymentMapper;
import com.winhxd.b2c.pay.dao.StoreBankrollMapper;
import com.winhxd.b2c.pay.service.PayService;

@Service
public class PayServiceImpl implements PayService{
	
	private static final Logger logger = LoggerFactory.getLogger(PayServiceImpl.class);
	@Autowired
	OrderServiceClient orderServiceClient;
	
	@Autowired
	PayOrderPaymentMapper payOrderPaymentMapper;
	
	@Autowired
	StoreBankrollMapper storeBankrollMapper;
	
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
		String log=logLabel+"订单支付支付orderPay";
		logger.info(log+"--开始");
		if (condition==null||StringUtils.isBlank(condition.getOrderNo())) {
			logger.info(log+"--参数为空");
			throw new BusinessException(BusinessCode.CODE_600102);
		}
		logger.info(log+"--参数"+condition.toString());
		//todo 调取微信支付接口  
		return null;
	}

	@Override
	public Integer callbackOrderPay(OrderPayCallbackCondition condition) {
		String log=logLabel+"支付回调callbackOrderPay";
		logger.info(log+"--开始");
		if (condition==null) {
			logger.info(log+"--参数为空");
			throw new BusinessException(BusinessCode.CODE_600101);
		}
		
		// 插入流水号
		PayOrderPayment payOrderPayment=new PayOrderPayment();
		int insertResult=payOrderPaymentMapper.insertSelective(payOrderPayment);
		if (insertResult<1) {
			//订单更新失败
			logger.info(log+"--订单支付流水插入失败");
//			throw new BusinessException(BusinessCode.CODE_600301);
		}

		return null;
	}


	@Override
	@Transactional
	public Integer callbackOrderRefund(UpdateOrderCondition condition) {
		String log=logLabel+"退款回调callbackOrderRefund";
		logger.info(log+"--开始");
		if (condition==null) {
			logger.info(log+"--参数为空");
			throw new BusinessException(BusinessCode.CODE_600303);
		}
		logger.info(log+"--参数"+condition.toString());
		//插入流水数据
		PayOrderPayment payOrderPayment=new PayOrderPayment();
		int insertResult=payOrderPaymentMapper.insertSelective(payOrderPayment);
		if (insertResult<1) {
			logger.info(log+"--订单退款流水插入失败");
//			throw new BusinessException(BusinessCode.CODE_600301);
		}
		//根据退款状态  判断是否更新订单状态
		//更新订单状态
		OrderRefundCallbackCondition orderRefundCallbackCondition=new OrderRefundCallbackCondition();
		orderRefundCallbackCondition.setOrderNo(condition.getOrderNo());
		ResponseResult<Boolean> callbackResult=orderServiceClient.updateOrderRefundCallback(orderRefundCallbackCondition);
		if (callbackResult.getCode()!=0&&!callbackResult.getData()) {
			//订单更新失败
			logger.info(log+"--订单更新失败");
			throw new BusinessException(BusinessCode.CODE_600301);
		}
		
         List<OrderRefundCallbackCondition> aaList=new ArrayList<>();
		
		logger.info(log+"--结束");
		return null;
	}

	@Override
	public void updateStoreBankroll(StoreBankrollChangeCondition condition) {
		String log=logLabel+"门店资金变化updateStoreBankroll";
		if (condition==null) {
			logger.info(log+"--参数为空");
			throw new BusinessException();
		}
		if (condition.getStoreId()==null) {
			logger.info(log+"--参数门店id为空");
			throw new BusinessException();
		}
		StoreBankroll storeBankroll=storeBankrollMapper.selectStoreBankrollByStoreId(condition.getStoreId());
		BigDecimal totalMoney=condition.getTotalMoeny()==null?BigDecimal.valueOf(0):condition.getTotalMoeny();
		BigDecimal presentedFrozenMoney=condition.getPresentedFrozenMoney()==null?BigDecimal.valueOf(0):condition.getPresentedFrozenMoney();
		BigDecimal presentedMoney=condition.getPresentedMoney()==null?BigDecimal.valueOf(0):condition.getPresentedMoney();
		BigDecimal settlementSettledMoney=condition.getSettlementSettledMoney()==null?BigDecimal.valueOf(0):condition.getSettlementSettledMoney();
		if (storeBankroll==null) {
			storeBankroll=new StoreBankroll();
			BeanUtils.copyProperties(condition, storeBankroll);
			storeBankrollMapper.insertSelective(storeBankroll);
		}else {
			totalMoney=storeBankroll.getTotalMoeny().add(totalMoney);
			presentedFrozenMoney=storeBankroll.getPresentedFrozenMoney().add(presentedFrozenMoney);
			presentedMoney=storeBankroll.getPresentedMoney().add(presentedMoney);
			settlementSettledMoney=storeBankroll.getSettlementSettledMoney().add(settlementSettledMoney);
			
			totalMoney=totalMoney.compareTo(BigDecimal.valueOf(0))<0?BigDecimal.valueOf(0):totalMoney;
			presentedFrozenMoney=presentedFrozenMoney.compareTo(BigDecimal.valueOf(0))<0?BigDecimal.valueOf(0):presentedFrozenMoney;
			presentedMoney=presentedMoney.compareTo(BigDecimal.valueOf(0))<0?BigDecimal.valueOf(0):presentedMoney;
			settlementSettledMoney=settlementSettledMoney.compareTo(BigDecimal.valueOf(0))<0?BigDecimal.valueOf(0):settlementSettledMoney;
			
			storeBankroll.setTotalMoeny(totalMoney);
			storeBankroll.setPresentedFrozenMoney(presentedFrozenMoney);
			storeBankroll.setPresentedMoney(presentedMoney);;
			storeBankroll.setSettlementSettledMoney(settlementSettledMoney);
			storeBankrollMapper.updateByPrimaryKeySelective(storeBankroll);
		}
		
	}
}
