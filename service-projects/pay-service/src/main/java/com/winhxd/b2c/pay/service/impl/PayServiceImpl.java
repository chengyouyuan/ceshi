package com.winhxd.b2c.pay.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.order.condition.OrderRefundCallbackCondition;
import com.winhxd.b2c.common.domain.pay.condition.OrderPayCallbackCondition;
import com.winhxd.b2c.common.domain.pay.condition.PayPreOrderCondition;
import com.winhxd.b2c.common.domain.pay.condition.PayRefundCondition;
import com.winhxd.b2c.common.domain.pay.condition.PayTransfersToWxBankCondition;
import com.winhxd.b2c.common.domain.pay.condition.PayTransfersToWxChangeCondition;
import com.winhxd.b2c.common.domain.pay.condition.StoreBankrollChangeCondition;
import com.winhxd.b2c.common.domain.pay.condition.UpdateOrderCondition;
import com.winhxd.b2c.common.domain.pay.condition.UpdateStoreBankRollCondition;
import com.winhxd.b2c.common.domain.pay.model.PayOrderPayment;
import com.winhxd.b2c.common.domain.pay.model.PayStoreBankrollLog;
import com.winhxd.b2c.common.domain.pay.model.StoreBankroll;
import com.winhxd.b2c.common.domain.pay.vo.OrderPayVO;
import com.winhxd.b2c.common.domain.pay.vo.PayRefundVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.order.OrderServiceClient;
import com.winhxd.b2c.pay.dao.PayOrderPaymentMapper;
import com.winhxd.b2c.pay.dao.PayStoreBankrollLogMapper;
import com.winhxd.b2c.pay.dao.StoreBankrollMapper;
import com.winhxd.b2c.pay.service.PayService;
import com.winhxd.b2c.pay.weixin.service.WXRefundService;
import com.winhxd.b2c.pay.weixin.service.WXTransfersService;
import com.winhxd.b2c.pay.weixin.service.WXUnifiedOrderService;

@Service
public class PayServiceImpl implements PayService{
	
	private static final Logger logger = LoggerFactory.getLogger(PayServiceImpl.class);
	@Autowired
	private OrderServiceClient orderServiceClient;
	
	@Autowired
	private PayOrderPaymentMapper payOrderPaymentMapper;
	
	@Autowired
	private StoreBankrollMapper storeBankrollMapper;
	@Autowired
	private PayStoreBankrollLogMapper payStoreBankrollLogMapper;
	@Autowired
	private WXUnifiedOrderService unifiedOrderService;
	@Autowired
	private WXRefundService refundService;
	@Autowired
	private WXTransfersService transfersService;
	
	private static final String logLabel="PayServiceImpl--";

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
//			throw new BusinessException(BusinessCode.CODE_600303);
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
	public void updateStoreBankroll(UpdateStoreBankRollCondition condition){
		String log=logLabel+"门店资金变化updateStoreBankroll";
		if (condition==null) {
			logger.info(log+"--参数为空");
			throw new BusinessException(BusinessCode.CODE_600001);
		}
		if (condition.getStoreId()==null) {
			logger.info(log+"--门店id为空");
			throw new BusinessException(BusinessCode.CODE_600002);
		}
		if (condition.getType()==null) {
			logger.info(log+"--操作类型为空");
			throw new BusinessException(BusinessCode.CODE_600003);
		}
		if (condition.getType().equals(1)) {
			
		}
		
	}
	
	public void storeBankrollChange(StoreBankrollChangeCondition condition) {
		StoreBankroll storeBankroll=storeBankrollMapper.selectStoreBankrollByStoreId(condition.getStoreId());
		BigDecimal presentedFrozenMoney=condition.getPresentedFrozenMoney()==null?BigDecimal.valueOf(0):condition.getPresentedFrozenMoney();
		BigDecimal presentedMoney=condition.getPresentedMoney()==null?BigDecimal.valueOf(0):condition.getPresentedMoney();
		BigDecimal settlementSettledMoney=condition.getSettlementSettledMoney()==null?BigDecimal.valueOf(0):condition.getSettlementSettledMoney();
		
		BigDecimal totalMoney=settlementSettledMoney;
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
			storeBankroll.setPresentedMoney(presentedMoney);
			storeBankroll.setSettlementSettledMoney(settlementSettledMoney);
			storeBankrollMapper.updateByPrimaryKeySelective(storeBankroll);
		}
		//加入资金流转日志
		saveStoreBankRollLog(condition);
		
	}

	public void saveStoreBankRollLog(StoreBankrollChangeCondition condition) {
		
		PayStoreBankrollLog payStoreBankrollLog = new PayStoreBankrollLog();
		BigDecimal presentedMoney=condition.getPresentedMoney();
		BigDecimal presentedFrozenMoney=condition.getPresentedFrozenMoney();
		BigDecimal settlementSettledMoney=condition.getSettlementSettledMoney();
		String remarks="";
		if(1 == condition.getType()){
			 remarks = "订单完成:总收入增加"+settlementSettledMoney +"元,待结算金额增加"+settlementSettledMoney+"元";
		}

		if(2 == condition.getType()){
			 remarks = "结算审核：待结算减少"+settlementSettledMoney +"元,可提现金额增加"+presentedMoney+"元";
		}

		if(3 == condition.getType()){
			 remarks = "提现申请:可提现金额减少"+presentedMoney +"元,提现冻结金额增加"+presentedFrozenMoney+"元";
		}

		if(4 == condition.getType()){
			 remarks = "提现审核:提现冻结金额减少"+presentedFrozenMoney +"元";
		}
		payStoreBankrollLog.setOrderNo(condition.getOrderNo());
		payStoreBankrollLog.setStoreId(condition.getStoreId());
		payStoreBankrollLog.setTotalMoeny(settlementSettledMoney);
		payStoreBankrollLog.setPresentedMoney(presentedMoney);
		payStoreBankrollLog.setSettlementSettledMoney(settlementSettledMoney);
		payStoreBankrollLog.setPresentedFrozenMoney(presentedFrozenMoney);
		payStoreBankrollLog.setWithdrawalsNo(condition.getWithdrawalsNo());
		payStoreBankrollLog.setRemarks(remarks);
		payStoreBankrollLogMapper.insertSelective(payStoreBankrollLog);
	}

	@Override
	public OrderPayVO unifiedOrder(PayPreOrderCondition condition) {
		//验证订单支付参数
		String log=logLabel+"订单支付unifiedOrder";
		logger.info(log+"--开始");
		if (condition==null){
			logger.info(log+"--参数为空");
			throw new BusinessException(BusinessCode.CODE_600101);
		}
		String orderNo=condition.getOutOrderNo();
		String spbillCreateIp=condition.getSpbillCreateIp();
		String body=condition.getBody();
		String openid=condition.getOpenid();
		if (StringUtils.isBlank(orderNo)) {
			logger.info(log+"--订单号为空");
			throw new BusinessException(BusinessCode.CODE_600102);
		}
		if (StringUtils.isBlank(body)) {
			logger.info(log+"--商品描述为空");
			throw new BusinessException(BusinessCode.CODE_600103);
		}
	
		if (StringUtils.isBlank(openid)) {
			logger.info(log+"--用户openid为空");
			throw new BusinessException(BusinessCode.CODE_600104);
		}
		if (StringUtils.isBlank(spbillCreateIp)) {
			logger.info(log+"--设备ip为空");
			throw new BusinessException(BusinessCode.CODE_600105);
		}
		logger.info(log+"--参数"+condition.toString());
		
		return unifiedOrderService.unifiedOrder(condition);
	}

	@Override
	public PayRefundVO refundOrder(PayRefundCondition payRefund) {
		
		//验证订单支付参数
		String log=logLabel+"订单退款refundOrder";
		logger.info(log+"--开始");
		if (payRefund==null){
			logger.info(log+"--参数为空");
			throw new BusinessException(BusinessCode.CODE_600201);
		}
		String orderNo=payRefund.getOutTradeNo();
		String appid=payRefund.getAppid();
		BigDecimal totalAmount=payRefund.getTotalAmount();
		BigDecimal refundAmount=payRefund.getRefundAmount();
		Long createdBy=payRefund.getCreatedBy();
		String createdByName=payRefund.getCreatedByName();
//		String refundDesc=payRefund.getRefundDesc();
		if (StringUtils.isBlank(orderNo)) {
			logger.info(log+"--订单号为空");
			throw new BusinessException(BusinessCode.CODE_600202);
		}
		if (StringUtils.isBlank(appid)) {
			logger.info(log+"--appid为空");
			throw new BusinessException(BusinessCode.CODE_600203);
		}
		if (totalAmount==null) {
			logger.info(log+"--订单金额为空");
			throw new BusinessException(BusinessCode.CODE_600204);
		}
		if (refundAmount==null) {
			logger.info(log+"--退款金额为空");
			throw new BusinessException(BusinessCode.CODE_600205);
		}
		if (createdBy==null) {
			logger.info(log+"--创建人为空");
			throw new BusinessException(BusinessCode.CODE_600206);
		}
		if (StringUtils.isBlank(createdByName)) {
			logger.info(log+"--创建人姓名为空");
			throw new BusinessException(BusinessCode.CODE_600207);
		}
		logger.info(log+"--参数"+payRefund.toString());
		
		return refundService.refundOrder(payRefund);
	}

	@Override
	public String transfersToChange(PayTransfersToWxChangeCondition toWxBalanceCondition) {
		
		return"";
//		return transfersService.transfersToChange(toWxBalanceCondition);
	}

	@Override
	public String transfersToBank(PayTransfersToWxBankCondition toWxBankCondition) {
//		return transfersService.transfersToBank(toWxBankCondition);
		return "";
	}
}
