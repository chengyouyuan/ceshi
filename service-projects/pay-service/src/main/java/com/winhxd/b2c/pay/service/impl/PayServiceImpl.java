package com.winhxd.b2c.pay.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.winhxd.b2c.common.domain.pay.condition.*;
import com.winhxd.b2c.common.domain.pay.model.PayStoreBankrollLog;
import com.winhxd.b2c.pay.dao.PayStoreBankrollLogMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.context.CustomerUser;
import com.winhxd.b2c.common.context.UserContext;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.common.ApiCondition.MobileInfo;
import com.winhxd.b2c.common.domain.order.condition.OrderRefundCallbackCondition;
import com.winhxd.b2c.common.domain.order.enums.OrderStatusEnum;
import com.winhxd.b2c.common.domain.order.vo.OrderInfoDetailVO;
import com.winhxd.b2c.common.domain.order.vo.OrderInfoDetailVO4Management;
import com.winhxd.b2c.common.domain.order.vo.OrderItemVO;
import com.winhxd.b2c.common.domain.pay.model.PayOrderPayment;
import com.winhxd.b2c.common.domain.pay.model.StoreBankroll;
import com.winhxd.b2c.common.domain.pay.vo.OrderPayVO;
import com.winhxd.b2c.common.domain.pay.vo.OrderRefundVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.order.OrderServiceClient;
import com.winhxd.b2c.pay.dao.PayOrderPaymentMapper;
import com.winhxd.b2c.pay.dao.StoreBankrollMapper;
import com.winhxd.b2c.pay.service.PayService;
import com.winhxd.b2c.pay.weixin.condition.PayPreOrderCondition;

@Service
public class PayServiceImpl implements PayService{
	
	private static final Logger logger = LoggerFactory.getLogger(PayServiceImpl.class);
	@Autowired
	OrderServiceClient orderServiceClient;
	
	@Autowired
	PayOrderPaymentMapper payOrderPaymentMapper;
	
	@Autowired
	StoreBankrollMapper storeBankrollMapper;
	@Autowired
	PayStoreBankrollLogMapper payStoreBankrollLogMapper;
	
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
		if (condition==null){
			logger.info(log+"--参数为空");
			throw new BusinessException(BusinessCode.CODE_600102);
		}
		String orderNo=condition.getOrderNo();
		String spbillCreateIp=condition.getSpbillCreateIp();
		if (StringUtils.isBlank(orderNo)) {
			logger.info(log+"--订单号为空");
			throw new BusinessException(BusinessCode.CODE_600107);
		}
		if (StringUtils.isBlank(spbillCreateIp)) {
			logger.info(log+"--设备ip为空");
			throw new BusinessException(BusinessCode.CODE_600108);
		}
		logger.info(log+"--参数"+condition.toString());
		CustomerUser customerUser=UserContext.getCurrentCustomerUser();
		if (customerUser==null) {
			logger.info(log+"--未获取到用户信息");
			throw new BusinessException(BusinessCode.CODE_600105);
		}
		String openid=customerUser.getOpenid();
		if (StringUtils.isBlank(openid)) {
			logger.info(log+"--未获取到用户openid");
			throw new BusinessException(BusinessCode.CODE_600106);
		}
		//根据订单号获取订单信息
		ResponseResult<OrderInfoDetailVO4Management> orderResult=orderServiceClient.getOrderDetail4Management(orderNo);
		OrderInfoDetailVO4Management info=orderResult.getData();
		if (info==null) {
			logger.info(log+"--未获取到订单数据");
			throw new BusinessException(BusinessCode.CODE_600103);
		}
		OrderInfoDetailVO orderInfo=info.getOrderInfoDetailVO();
		if (orderInfo==null) {
			logger.info(log+"---未获取到订单数据");
			throw new BusinessException(BusinessCode.CODE_600103);
		}
		if (orderInfo.getOrderStatus()!=OrderStatusEnum.WAIT_PAY.getStatusCode()) {
			logger.info(log+"---订单状态有误");
			throw new BusinessException(BusinessCode.CODE_600104);
		}
		List<OrderItemVO> itemVOs=orderInfo.getOrderItemVoList();
		StringBuilder body=new StringBuilder();
		if (CollectionUtils.isNotEmpty(itemVOs)) {
			for (OrderItemVO orderItemVO : itemVOs) {
				body.append(orderItemVO.getSkuDesc());
				body.append("*");
				body.append(orderItemVO.getAmount());
				body.append(";");
			}
		}
		String deviceInfo="";
		MobileInfo mobileInfo=condition.getMobileInfo();
		
		if (mobileInfo!=null) {
			deviceInfo=mobileInfo.getImei();
		}
		
		//组装支付信息
		
		PayPreOrderCondition payPreOrderCondition=new PayPreOrderCondition();
		payPreOrderCondition.setDeviceInfo(deviceInfo);
		payPreOrderCondition.setBody(body.toString());
		payPreOrderCondition.setOpenid(openid);
		payPreOrderCondition.setOutOrderNo(orderNo);
		payPreOrderCondition.setTotalAmount(orderInfo.getRealPaymentMoney());
		payPreOrderCondition.setSpbillCreateIp(spbillCreateIp);
		
		
		//todo 调取微信支付接口  
		return null;
	}

	@Override
	@Transactional
	public Integer callbackOrderPay(OrderPayCallbackCondition condition) {
		String log=logLabel+"支付回调callbackOrderPay";
		logger.info(log+"--开始");
		if (condition==null) {
			logger.info(log+"--参数为空");
			throw new BusinessException(BusinessCode.CODE_600101);
		}
		//todo判断支付成功之后更新订单信息
        orderServiceClient.orderPaySuccessNotify("订单号", "交易号");
		// 更新流水号
		PayOrderPayment payOrderPayment=new PayOrderPayment();
		int insertResult=payOrderPaymentMapper.updateByPrimaryKeySelective(payOrderPayment);
		if (insertResult<1) {
			//订单更新失败
			logger.info(log+"--订单支付流更新失败");
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
			storeBankroll.setTotalMoeny(totalMoney);
			storeBankroll.setPresentedFrozenMoney(presentedFrozenMoney);
			storeBankroll.setPresentedMoney(presentedMoney);;
			storeBankroll.setSettlementSettledMoney(settlementSettledMoney);
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

	@Override
	public void saveStoreBankRollLog(StoreBankRollLogCondition condition) {
		String log=logLabel+"记录用户资金流转日志saveStoreBankRollLog";
		if (condition==null) {
			logger.info(log+"--参数为空");
			throw new BusinessException();
		}
		if (condition.getStoreId()==null) {
			logger.info(log+"--参数门店id为空");
			throw new BusinessException();
		}
		PayStoreBankrollLog payStoreBankrollLog = new PayStoreBankrollLog();
		BigDecimal orderMoeny=condition.getOrderMoeny()==null?BigDecimal.valueOf(0):condition.getOrderMoeny();
		BigDecimal presentedMoney=condition.getPresentedMoney()==null?BigDecimal.valueOf(0):condition.getPresentedMoney();
		BigDecimal settlementMoney=condition.getSettlementMoney()==null?BigDecimal.valueOf(0):condition.getSettlementMoney();

		if(1 == condition.getType()){
			if (condition.getStoreId()==null) {
				logger.info(log+"--订单完成:参数订单号为空");
				throw new BusinessException();
			}
			String remarks = "订单完成:总收入增加"+orderMoeny +"元,待结算金额增加"+orderMoeny+"元";

			payStoreBankrollLog.setOrderNo(condition.getOrderNo());
			payStoreBankrollLog.setStoreId(condition.getStoreId());
			payStoreBankrollLog.setTotalMoeny(orderMoeny);
			payStoreBankrollLog.setSettlementSettledMoney(orderMoeny);
			payStoreBankrollLog.setRemarks(remarks);
			payStoreBankrollLogMapper.insertSelective(payStoreBankrollLog);
		}

		if(2 == condition.getType()){
			if (condition.getStoreId()==null) {
				logger.info(log+"--结算审核:参数订单号为空");
				throw new BusinessException();
			}
			String remarks = "结算审核：待结算减少"+settlementMoney +"元,可提现金额增加"+settlementMoney+"元";

			payStoreBankrollLog.setOrderNo(condition.getOrderNo());
			payStoreBankrollLog.setStoreId(condition.getStoreId());
			payStoreBankrollLog.setPresentedMoney(settlementMoney);
			payStoreBankrollLog.setSettlementSettledMoney(settlementMoney);
			payStoreBankrollLog.setRemarks(remarks);
			payStoreBankrollLogMapper.insertSelective(payStoreBankrollLog);
		}

		if(3 == condition.getType()){
			if (condition.getStoreId()==null) {
				logger.info(log+"--提现申请:参数提现单号为空");
				throw new BusinessException();
			}
			String remarks = "提现申请:可提现金额减少"+presentedMoney +"元,提现冻结金额增加"+presentedMoney+"元";

			payStoreBankrollLog.setStoreId(condition.getStoreId());
			payStoreBankrollLog.setWithdrawalsNo(condition.getWithdrawalsNo());
			payStoreBankrollLog.setPresentedMoney(presentedMoney);
			payStoreBankrollLog.setPresentedFrozenMoney(presentedMoney);
			payStoreBankrollLog.setRemarks(remarks);
			payStoreBankrollLogMapper.insertSelective(payStoreBankrollLog);
		}

		if(4 == condition.getType()){
			if (condition.getWithdrawalsNo()==null) {
				logger.info(log+"--提现审核:参数提现单号为空");
				throw new BusinessException();
			}
			String remarks = "提现审核:提现冻结金额减少"+presentedMoney +"元";

			payStoreBankrollLog.setWithdrawalsNo(condition.getWithdrawalsNo());
			payStoreBankrollLog.setStoreId(condition.getStoreId());
			payStoreBankrollLog.setPresentedMoney(presentedMoney);
			payStoreBankrollLog.setPresentedFrozenMoney(presentedMoney);
			payStoreBankrollLog.setRemarks(remarks);
			payStoreBankrollLogMapper.insertSelective(payStoreBankrollLog);
		}
	}
}
