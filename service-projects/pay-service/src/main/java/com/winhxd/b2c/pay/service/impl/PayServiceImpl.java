package com.winhxd.b2c.pay.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.winhxd.b2c.common.domain.pay.enums.TradeTypeEnums;
import com.winhxd.b2c.pay.weixin.model.PayBill;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.context.StoreUser;
import com.winhxd.b2c.common.context.UserContext;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.order.condition.OrderRefundCallbackCondition;
import com.winhxd.b2c.common.domain.pay.condition.OrderPayCallbackCondition;
import com.winhxd.b2c.common.domain.pay.condition.PayPreOrderCondition;
import com.winhxd.b2c.common.domain.pay.condition.PayRefundCondition;
import com.winhxd.b2c.common.domain.pay.condition.PayTransfersToWxBankCondition;
import com.winhxd.b2c.common.domain.pay.condition.PayTransfersToWxChangeCondition;
import com.winhxd.b2c.common.domain.pay.condition.StoreBankrollChangeCondition;
import com.winhxd.b2c.common.domain.pay.condition.StoreBindStoreWalletCondition;
import com.winhxd.b2c.common.domain.pay.condition.UpdateStoreBankRollCondition;
import com.winhxd.b2c.common.domain.pay.enums.StoreBankRollOpearateEnums;
import com.winhxd.b2c.common.domain.pay.model.PayFinanceAccountDetail;
import com.winhxd.b2c.common.domain.pay.model.PayOrderPayment;
import com.winhxd.b2c.common.domain.pay.model.PayRefundPayment;
import com.winhxd.b2c.common.domain.pay.model.PayStoreBankrollLog;
import com.winhxd.b2c.common.domain.pay.model.PayStoreWallet;
import com.winhxd.b2c.common.domain.pay.model.StoreBankroll;
import com.winhxd.b2c.common.domain.pay.vo.OrderPayVO;
import com.winhxd.b2c.common.domain.pay.vo.PayRefundVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.order.OrderServiceClient;
import com.winhxd.b2c.pay.dao.PayFinanceAccountDetailMapper;
import com.winhxd.b2c.pay.dao.PayOrderPaymentMapper;
import com.winhxd.b2c.pay.dao.PayRefundPaymentMapper;
import com.winhxd.b2c.pay.dao.PayStoreBankrollLogMapper;
import com.winhxd.b2c.pay.dao.PayStoreWalletMapper;
import com.winhxd.b2c.pay.dao.StoreBankrollMapper;
import com.winhxd.b2c.pay.service.PayService;
import com.winhxd.b2c.pay.weixin.model.PayRefund;
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
	private PayRefundPaymentMapper payRefundPaymentMapper;
	@Autowired
	private PayFinanceAccountDetailMapper payFinanceAccountDetailMapper;
	@Autowired
	private WXUnifiedOrderService unifiedOrderService;
	@Autowired
	private WXRefundService refundService;
	@Autowired
	private WXTransfersService transfersService;
	
	@Autowired
	private PayStoreWalletMapper payStoreWalletMapper;
	
	private static final String logLabel="PayServiceImpl--";

	@Override
	@Transactional
	public Integer callbackOrderPay(PayBill condition) {
		String log=logLabel+"支付回调callbackOrderPay";
		logger.info(log+"--开始");
		if (condition==null) {
			logger.info(log+"--参数为空");
			throw new BusinessException(BusinessCode.CODE_600101);
		}
		if (StringUtils.isBlank(condition.getOutOrderNo())) {
			logger.info(log+"--订单号为空");
			throw new BusinessException(BusinessCode.CODE_600004);
		}
		//判断支付成功之后更新订单信息
		if(1 == condition.getStatus()){
			orderServiceClient.orderPaySuccessNotify(condition.getOutOrderNo(),condition.getOutTradeNo());
		}

		// 更新流水号
		PayOrderPayment payOrderPayment=new PayOrderPayment();
		payOrderPayment.setOrderNo(condition.getOutOrderNo());
		payOrderPayment.setOrderPamentNo(condition.getOutTradeNo());
		payOrderPayment.setCallbackDate(new Date());
		payOrderPayment.setCreated(new Date());
		payOrderPayment.setTimeEnd(condition.getTimeEnd());
		payOrderPayment.setCallbackStatus(condition.getStatus());
		payOrderPayment.setCallbackReason(condition.getErrorCode());
		payOrderPayment.setBuyerId(condition.getBuyerId());
		payOrderPayment.setTransactionId(condition.getTransactionId());
		payOrderPayment.setRealPaymentMoney(condition.getTotalAmount());
		payOrderPayment.setCallbackMoney(condition.getCallbackTotalAmount());
		if(condition.getTradeType().equals(TradeTypeEnums.JSAPI.getCode())){
			payOrderPayment.setPayType((short) 1);
		}
		if(condition.getTradeType().equals(TradeTypeEnums.NATIVE.getCode())){
			payOrderPayment.setPayType((short) 2);
		}
		if(condition.getTradeType().equals(TradeTypeEnums.APP.getCode())){
			payOrderPayment.setPayType((short) 3);
		}
		int insertResult=payOrderPaymentMapper.insertSelective(payOrderPayment);
		if (insertResult<1) {
			//订单更新失败
			logger.info(log+"--订单支付流更新失败");
//			throw new BusinessException(BusinessCode.CODE_600301);
		}

		return insertResult;
	}


	@Override
	@Transactional
	public Integer callbackOrderRefund(PayRefund condition) {
		String log=logLabel+"退款回调callbackOrderRefund";
		logger.info(log+"--开始");
		if (condition==null) {
			logger.info(log+"--参数为空");
			throw new BusinessException(BusinessCode.CODE_600101);
		}
		if (StringUtils.isBlank(condition.getOrderNo())) {
			logger.info(log+"--订单号为空");
			throw new BusinessException(BusinessCode.CODE_600004);
		}
		logger.info(log+"--参数"+condition.toString());
		//插入流水数据
		PayRefundPayment payRefundPayment=new PayRefundPayment();
		payRefundPayment.setOrderNo(condition.getOrderNo());
		payRefundPayment.setOrderTransactionNo(condition.getOutTradeNo());
		payRefundPayment.setRefundNo(condition.getOrderNo());
		payRefundPayment.setRefundTransactionNo(condition.getOutRefundNo());
		payRefundPayment.setCallbackDate(new Date());
		payRefundPayment.setTimeEnd(condition.getCallbackSuccessTime());
		payRefundPayment.setCallbackStatus(condition.getCallbackRefundStatus());
		payRefundPayment.setCallbackReason(condition.getRefundDesc());
		payRefundPayment.setTransactionId(condition.getCallbackRefundId());
		payRefundPayment.setRefundFee(condition.getRefundAmount());
		payRefundPayment.setCallbackMoney(condition.getCallbackRefundAmount());
		payRefundPayment.setPayType(condition.getPayType());
		payRefundPayment.setCreated(new Date());
		int insertResult=payRefundPaymentMapper.insertSelective(payRefundPayment);
		if (insertResult<1) {
			logger.info(log+"--订单退款流水插入失败");
//			throw new BusinessException(BusinessCode.CODE_600301);
		}
		//根据退款状态  判断是否更新订单状态
		if(1 == condition.getCallbackRefundStatus()){
			//更新订单状态
			OrderRefundCallbackCondition orderRefundCallbackCondition=new OrderRefundCallbackCondition();
			orderRefundCallbackCondition.setOrderNo(condition.getOrderNo());
			ResponseResult<Boolean> callbackResult=orderServiceClient.updateOrderRefundCallback(orderRefundCallbackCondition);
			if (callbackResult.getCode()!=0&&!callbackResult.getData()) {
				//订单更新失败
				logger.info(log+"--订单更新失败");
				throw new BusinessException(BusinessCode.CODE_600301);
			}
		}

		//出账明细表 pay_finance_account_detail
		PayFinanceAccountDetail payFinanceAccountDetail = new PayFinanceAccountDetail();
		payFinanceAccountDetail.setOrderNo(condition.getOrderNo());
		payFinanceAccountDetail.setOutType((short) 2);
		payFinanceAccountDetail.setStatus((short) 1);
		payFinanceAccountDetail.setCreated(new Date());
		payFinanceAccountDetail.setTradeNo(condition.getOutRefundNo());
		int payFinanceInsertResult = payFinanceAccountDetailMapper.insertSelective(payFinanceAccountDetail);
		if (payFinanceInsertResult<1) {
			logger.info(log+"--订单出账明细表插入失败");
//			throw new BusinessException(BusinessCode.CODE_600301);
		}
		List<OrderRefundCallbackCondition> aaList=new ArrayList<>();

		logger.info(log+"--结束");
		return 0;
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
		logger.info(log+"--参数"+condition.toString());
		boolean flag=false;
		if(StoreBankRollOpearateEnums.ORDER_FINISH.getCode().equals(condition.getType())){
			// 验证该订单是否已经做过此项操作
			String orderNo=condition.getOrderNo();
			if (StringUtils.isNotBlank(orderNo)) {
				logger.info(log+"--订单号为空");
				throw new BusinessException(BusinessCode.CODE_600004);
			}
		}

		if(StoreBankRollOpearateEnums.SETTLEMENT_AUDIT.getCode().equals(condition.getType())){
          //验证订单信息
			String orderNo=condition.getOrderNo();
			if (StringUtils.isNotBlank(orderNo)) {
				logger.info(log+"--订单号为空");
				throw new BusinessException(BusinessCode.CODE_600004);
			}
			//获取订单信息  如果订单状态不是完成状态  不可以进行此操作
			
			//验证该订单是否做过此项操作
			
		}

		if(StoreBankRollOpearateEnums.withdrawals_apply.getCode().equals(condition.getType())){
			//todo 验证该订单是否做过此项操作
			String withdrawalsNo=condition.getWithdrawalsNo();
			if (StringUtils.isNotBlank(withdrawalsNo)) {
				logger.info(log+"--提现单号为空");
				throw new BusinessException(BusinessCode.CODE_600005);
			}
		}

		if(StoreBankRollOpearateEnums.withdrawals_audit.getCode().equals(condition.getType())){
			//todo 验证该订单是否做过此项操作
			String withdrawalsNo=condition.getWithdrawalsNo();
			if (StringUtils.isNotBlank(withdrawalsNo)) {
				logger.info(log+"--提现单号为空");
				throw new BusinessException(BusinessCode.CODE_600005);
			}
		}
		if (flag) {
			
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
		if(StoreBankRollOpearateEnums.ORDER_FINISH.getCode().equals(condition.getType())){
			 remarks = "订单完成:总收入增加"+settlementSettledMoney +"元,待结算金额增加"+settlementSettledMoney+"元";
		}

		if(StoreBankRollOpearateEnums.SETTLEMENT_AUDIT.getCode().equals(condition.getType())){
			 remarks = "结算审核：待结算减少"+settlementSettledMoney +"元,可提现金额增加"+presentedMoney+"元";
		}

		if(StoreBankRollOpearateEnums.withdrawals_apply.getCode().equals(condition.getType())){
			 remarks = "提现申请:可提现金额减少"+presentedMoney +"元,提现冻结金额增加"+presentedFrozenMoney+"元";
		}

		if(StoreBankRollOpearateEnums.withdrawals_audit.getCode().equals(condition.getType())){
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
		//String appid=payRefund.getAppid();
		BigDecimal totalAmount=payRefund.getTotalAmount();
		BigDecimal refundAmount=payRefund.getRefundAmount();
		Long createdBy=payRefund.getCreatedBy();
		String createdByName=payRefund.getCreatedByName();
//		String refundDesc=payRefund.getRefundDesc();
		if (StringUtils.isBlank(orderNo)) {
			logger.info(log+"--订单号为空");
			throw new BusinessException(BusinessCode.CODE_600202);
		}
		/*if (StringUtils.isBlank(appid)) {
			logger.info(log+"--appid为空");
			throw new BusinessException(BusinessCode.CODE_600203);
		}*/
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
		
		return transfersService.transfersToChange(toWxBalanceCondition);
	}

	@Override
	public String transfersToBank(PayTransfersToWxBankCondition toWxBankCondition) {
		return transfersService.transfersToBank(toWxBankCondition);
	}


	@Override
	public List<PayStoreWallet> selectPayStoreWalletByStoreId() {
		String log=logLabel+"查询门店绑定钱包selectPayStoreWalletByStoreId";
		logger.info(log+"--开始");
		StoreUser storeUser=UserContext.getCurrentStoreUser();
		if (storeUser==null||storeUser.getBusinessId()==null) {
			logger.info(log+"--未获取到门店信息");
			throw new BusinessException(BusinessCode.CODE_600601);
		}
		return payStoreWalletMapper.selectByStoreId(storeUser.getBusinessId());
	}


	@Override
	public void storeBindStoreWallet(StoreBindStoreWalletCondition condition) {
		String log=logLabel+"门店绑定storeBindStoreWallet";
		logger.info(log+"--开始");
		StoreUser storeUser=UserContext.getCurrentStoreUser();
		if (storeUser==null||storeUser.getBusinessId()==null) {
			logger.info("--未获取到门店信息");
			throw new BusinessException(BusinessCode.CODE_600701);
		}
		if (condition==null) {
			logger.info("--参数为空");
			throw new BusinessException(BusinessCode.CODE_600702);
		}
		if (StringUtils.isBlank(condition.getOpenid())) {
			logger.info("--openid为空");
			throw new BusinessException(BusinessCode.CODE_600703);
		}
		if (StringUtils.isBlank(condition.getName())) {
			logger.info("--真实姓名为空");
			throw new BusinessException(BusinessCode.CODE_600704);
		}
		if (StringUtils.isBlank(condition.getNick())) {
			logger.info("--昵称为空");
			throw new BusinessException(BusinessCode.CODE_600705);
		}
		logger.info("--参数"+condition.toString());
		PayStoreWallet wallet=new PayStoreWallet();
		wallet.setStoreId(storeUser.getBusinessId());
		wallet.setName(condition.getName());
		wallet.setNick(condition.getNick());
		wallet.setOpenid(condition.getOpenid());
		wallet.setStatus((short)1);
		payStoreWalletMapper.insertSelective(wallet);
	}
}
