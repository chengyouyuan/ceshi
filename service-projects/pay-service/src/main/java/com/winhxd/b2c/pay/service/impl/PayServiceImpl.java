package com.winhxd.b2c.pay.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.winhxd.b2c.common.cache.Cache;
import com.winhxd.b2c.common.cache.Lock;
import com.winhxd.b2c.common.cache.RedisLock;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.CacheName;
import com.winhxd.b2c.common.context.StoreUser;
import com.winhxd.b2c.common.context.UserContext;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.order.condition.OrderRefundCallbackCondition;
import com.winhxd.b2c.common.domain.order.enums.PayStatusEnum;
import com.winhxd.b2c.common.domain.order.model.OrderInfo;
import com.winhxd.b2c.common.domain.pay.condition.OrderIsPayCondition;
import com.winhxd.b2c.common.domain.pay.condition.PayPreOrderCondition;
import com.winhxd.b2c.common.domain.pay.condition.PayRefundCondition;
import com.winhxd.b2c.common.domain.pay.condition.PayTransfersToWxBankCondition;
import com.winhxd.b2c.common.domain.pay.condition.PayTransfersToWxChangeCondition;
import com.winhxd.b2c.common.domain.pay.condition.StoreBankrollChangeCondition;
import com.winhxd.b2c.common.domain.pay.condition.UpdateStoreBankRollCondition;
import com.winhxd.b2c.common.domain.pay.enums.PayOutTypeEnum;
import com.winhxd.b2c.common.domain.pay.enums.PayRefundStatusEnums;
import com.winhxd.b2c.common.domain.pay.enums.PayStatusEnums;
import com.winhxd.b2c.common.domain.pay.enums.StoreBankRollOpearateEnums;
import com.winhxd.b2c.common.domain.pay.enums.StoreTransactionStatusEnum;
import com.winhxd.b2c.common.domain.pay.enums.WithdrawalsStatusEnum;
import com.winhxd.b2c.common.domain.pay.model.PayFinanceAccountDetail;
import com.winhxd.b2c.common.domain.pay.model.PayOrderPayment;
import com.winhxd.b2c.common.domain.pay.model.PayRefundPayment;
import com.winhxd.b2c.common.domain.pay.model.PayStoreBankrollLog;
import com.winhxd.b2c.common.domain.pay.model.PayStoreTransactionRecord;
import com.winhxd.b2c.common.domain.pay.model.PayStoreWallet;
import com.winhxd.b2c.common.domain.pay.model.PayWithdrawals;
import com.winhxd.b2c.common.domain.pay.model.StoreBankroll;
import com.winhxd.b2c.common.domain.pay.vo.PayPreOrderVO;
import com.winhxd.b2c.common.domain.pay.vo.PayRefundVO;
import com.winhxd.b2c.common.domain.pay.vo.PayTransfersToWxBankVO;
import com.winhxd.b2c.common.domain.pay.vo.PayTransfersToWxChangeVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.order.OrderServiceClient;
import com.winhxd.b2c.common.mq.event.EventMessageListener;
import com.winhxd.b2c.common.mq.event.EventMessageSender;
import com.winhxd.b2c.common.mq.event.EventType;
import com.winhxd.b2c.common.mq.event.EventTypeHandler;
import com.winhxd.b2c.pay.dao.PayFinanceAccountDetailMapper;
import com.winhxd.b2c.pay.dao.PayOrderPaymentMapper;
import com.winhxd.b2c.pay.dao.PayRefundPaymentMapper;
import com.winhxd.b2c.pay.dao.PayStoreBankrollLogMapper;
import com.winhxd.b2c.pay.dao.PayStoreWalletMapper;
import com.winhxd.b2c.pay.dao.PayWithdrawalsMapper;
import com.winhxd.b2c.pay.dao.StoreBankrollMapper;
import com.winhxd.b2c.pay.service.PayFinanceAccountDetailService;
import com.winhxd.b2c.pay.service.PayService;
import com.winhxd.b2c.pay.service.PayStoreCashService;
import com.winhxd.b2c.pay.weixin.model.PayBill;
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
	private PayWithdrawalsMapper payWithdrawalsMapper;
	@Autowired
	private WXUnifiedOrderService unifiedOrderService;
	@Autowired
	private WXRefundService refundService;
	@Autowired
	private WXTransfersService transfersService;
	
	@Autowired
	private PayStoreWalletMapper payStoreWalletMapper;
	@Autowired
	private PayStoreCashService payStoreCashService;
	@Autowired
	private PayFinanceAccountDetailService payFinanceAccountDetailService;
	
	 @Autowired
    private EventMessageSender eventMessageSender;
	
	@Autowired
    private Cache cache;
    private static final int BACKROLL_LOCK_EXPIRES_TIME = 3000;

	
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
		if(PayStatusEnums.PAY_SUCCESS.getCode().equals(condition.getStatus())){
			orderServiceClient.orderPaySuccessNotify(condition.getOutOrderNo(),condition.getOutTradeNo());
		}

		// 更新流水号
		PayOrderPayment payOrderPayment=new PayOrderPayment();
		payOrderPayment.setOrderTransactionNo(condition.getOutTradeNo());
		payOrderPayment.setCallbackDate(new Date());
		payOrderPayment.setUpdated(new Date());
		payOrderPayment.setTimeEnd(condition.getTimeEnd());
		payOrderPayment.setCallbackStatus(condition.getStatus());
		payOrderPayment.setCallbackErrorCode(condition.getErrorCode());
		payOrderPayment.setCallbackErrorReason(condition.getErrorMessage());
		payOrderPayment.setTransactionId(condition.getTransactionId());
		payOrderPayment.setCallbackMoney(condition.getCallbackTotalAmount());
		payOrderPayment.setAppid(condition.getAppid());
		payOrderPayment.setAttach(condition.getAttach());
		payOrderPayment.setBody(condition.getBody());
		payOrderPayment.setDetail(condition.getDetail());
		payOrderPayment.setDeviceInfo(condition.getDeviceInfo());
		payOrderPayment.setMchId(condition.getMchId());
		payOrderPayment.setNonceStr(condition.getNonceStr());
		payOrderPayment.setTimeStart(condition.getTimeStart());
		payOrderPayment.setTimeExpire(condition.getTimeExpire());
		int insertResult=payOrderPaymentMapper.updateByOrderTransactionNoSelective(payOrderPayment);
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
		payRefundPayment.setRefundTransactionNo(condition.getOutRefundNo());
		payRefundPayment.setCallbackDate(new Date());
		payRefundPayment.setCallbackStatus(condition.getCallbackRefundStatus());
		payRefundPayment.setCallbackMoney(condition.getCallbackRefundAmount());
		payRefundPayment.setPayType(condition.getPayType());
		payRefundPayment.setTransactionId(condition.getCallbackRefundId());
		payRefundPayment.setRefundFee(condition.getRefundAmount());
		payRefundPayment.setPayType(condition.getPayType());
		payRefundPayment.setAppid(condition.getAppid());
		payRefundPayment.setMchId(condition.getMchId());
		payRefundPayment.setNonceStr(condition.getNonceStr());
		payRefundPayment.setCallbackErrorCode(condition.getErrorCode());
		payRefundPayment.setCallbackErrorMessage(condition.getErrorMessage());
		payRefundPayment.setCallbackSuccessTime(condition.getCallbackSuccessTime());
		payRefundPayment.setCallbackRefundRecvAccout(condition.getCallbackRefundRecvAccout());
		payRefundPayment.setCallbackRefundAccount(condition.getCallbackRefundAccount());
		int insertResult=payRefundPaymentMapper.updateByRefundTransactionNoSelective(payRefundPayment);
		if (insertResult<1) {
			logger.info(log+"--订单退款流水更新失败");
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
		payFinanceAccountDetail.setOutType(PayOutTypeEnum.CUSTOMER_REFUND.getStatusCode());
		payFinanceAccountDetail.setStatus((short) 1);
		payFinanceAccountDetail.setCreated(new Date());
		payFinanceAccountDetail.setTradeNo(condition.getOutRefundNo());
        int payFinanceInsertResult = payFinanceAccountDetailService.saveFinanceAccountDetail(payFinanceAccountDetail);
		if (payFinanceInsertResult<1) {
			logger.info(log+"--订单出账明细表插入失败");
		}

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
		Long storeId=condition.getStoreId();
		if (condition.getStoreId()==null) {
			logger.info(log+"--门店id为空");
			throw new BusinessException(BusinessCode.CODE_600002);
		}
		if (condition.getType()==null) {
			logger.info(log+"--操作类型为空");
			throw new BusinessException(BusinessCode.CODE_600003);
		}
		if (condition.getMoney()==null) {
			logger.info(log+"--金额为空");
			throw new BusinessException(BusinessCode.CODE_600009);
		}
		if (condition.getMoney().compareTo(BigDecimal.valueOf(0))<0) {
			logger.info(log+"--金额有误");
			throw new BusinessException(BusinessCode.CODE_600010);
		}
		logger.info(log+"--参数"+condition.toString());
		boolean flag=false;
		Map<String, Object> map=new HashMap<>();
		map.put("type", condition.getType());
		StoreBankrollChangeCondition changeCondition=null;
		if(StoreBankRollOpearateEnums.ORDER_FINISH.getCode().equals(condition.getType())){
			// 验证该订单是否已经做过此项操作
			String orderNo=condition.getOrderNo();
			if (StringUtils.isNotBlank(orderNo)) {
				logger.info(log+"--订单闭环订单号为空");
				throw new BusinessException(BusinessCode.CODE_600004);
			}
			//（待定）不知是否需要  验证订单状态
			//验证该订单是否做过此项操作
			map.put("orderNo",orderNo);
			List<PayStoreBankrollLog> list=payStoreBankrollLogMapper.selectListByNoAndType(map);
			if (CollectionUtils.isNotEmpty(list)) {
				//说明在订单闭环的时候  已经给用户添加过资金
				logger.info(log+"--订单闭环计算用户资金重复--订单号"+orderNo);
				throw new BusinessException(BusinessCode.CODE_600006);
			}
			 changeCondition=new StoreBankrollChangeCondition();
			 changeCondition.setStoreId(storeId);
			 changeCondition.setType(condition.getType());
			 condition.setOrderNo(orderNo);
			 //待结算金额增加
			 changeCondition.setPresentedMoney(condition.getMoney());
			 flag=true;
		}

		if(StoreBankRollOpearateEnums.SETTLEMENT_AUDIT.getCode().equals(condition.getType())){
          //验证订单信息
			String orderNo=condition.getOrderNo();
			Short moneyType=condition.getMoneyType();
			if (StringUtils.isNotBlank(orderNo)) {
				logger.info(log+"--结算审核订单号为空");
				throw new BusinessException(BusinessCode.CODE_600004);
			}
			if (moneyType==null) {
				logger.info(log+"--结算审核费用类型为空");
				throw new BusinessException(BusinessCode.CODE_600007);
			}
			
			//验证该订单是否做过此项操作
			map.put("orderNo",orderNo);
			map.put("moneyType", moneyType);
			List<PayStoreBankrollLog> list=payStoreBankrollLogMapper.selectListByNoAndType(map);
			if (CollectionUtils.isNotEmpty(list)) {
				//说明在订单闭环的时候  已经给用户添加过资金
				logger.info(log+"--结算审核计算用户资金重复--订单号"+orderNo+"费用类型"+moneyType);
				throw new BusinessException(BusinessCode.CODE_600008);
			}
			 changeCondition=new StoreBankrollChangeCondition();
			 changeCondition.setType(condition.getType());
			 changeCondition.setOrderNo(orderNo);
			 changeCondition.setMoneyType(moneyType);
			 //待结算金额减少(因为用户资金变化都是add，这里需要传负数)
			 changeCondition.setSettlementSettledMoney(condition.getMoney().multiply(BigDecimal.valueOf(-1)));
			 //可提现金额增加
			 changeCondition.setPresentedMoney(condition.getMoney());
			 flag=true;
		
			
		}

		if(StoreBankRollOpearateEnums.WITHDRAWALS_APPLY.getCode().equals(condition.getType())){
			//todo 验证该订单是否做过此项操作
			String withdrawalsNo=condition.getWithdrawalsNo();
			if (StringUtils.isNotBlank(withdrawalsNo)) {
				logger.info(log+"--提现申请提现单号为空");
				throw new BusinessException(BusinessCode.CODE_600005);
			}
			map.put("withdrawalsNo",withdrawalsNo);
			List<PayStoreBankrollLog> list=payStoreBankrollLogMapper.selectListByNoAndType(map);
			if (CollectionUtils.isNotEmpty(list)) {
				//说明在订单闭环的时候  已经给用户添加过资金
				logger.info(log+"--提现申请计算用户资金重复--提现单号"+withdrawalsNo);
				throw new BusinessException(BusinessCode.CODE_600011);
			}
			 changeCondition=new StoreBankrollChangeCondition();
			 changeCondition.setType(condition.getType());
			 changeCondition.setWithdrawalsNo(withdrawalsNo);
			 //可提现金额减少(因为用户资金变化都是add，这里需要传负数)
			 changeCondition.setPresentedMoney(condition.getMoney().multiply(BigDecimal.valueOf(-1)));
			 //冻结金额增加
			 changeCondition.setPresentedFrozenMoney(condition.getMoney());
			 flag=true;
			
		}
		if(StoreBankRollOpearateEnums.WITHDRAWALS_AUDIT_NOT_PASS.getCode().equals(condition.getType())){
			// 验证该订单是否做过此项操作
			String withdrawalsNo=condition.getWithdrawalsNo();
			if (StringUtils.isNotBlank(withdrawalsNo)) {
				logger.info(log+"--提现审核不通过 提现单号为空");
				throw new BusinessException(BusinessCode.CODE_600005);
			}
			map.put("withdrawalsNo",withdrawalsNo);
			List<PayStoreBankrollLog> list=payStoreBankrollLogMapper.selectListByNoAndType(map);
			if (CollectionUtils.isNotEmpty(list)) {
				//说明已经做过此操作
				logger.info(log+"--提现审核不通过计算用户资金重复--提现单号"+withdrawalsNo);
				throw new BusinessException(BusinessCode.CODE_600012);
			}
			changeCondition=new StoreBankrollChangeCondition();
			changeCondition.setType(condition.getType());
			changeCondition.setWithdrawalsNo(withdrawalsNo);
			//可提现金额增加
			changeCondition.setPresentedMoney(condition.getMoney());
			//冻结金额减少(因为用户资金变化都是add，这里需要传负数)
			changeCondition.setPresentedFrozenMoney(condition.getMoney().multiply(BigDecimal.valueOf(-1)));
			flag=true;
			
		}
		if(StoreBankRollOpearateEnums.WITHDRAWALS_FAIL.getCode().equals(condition.getType())){
			// 验证该订单是否做过此项操作
			String withdrawalsNo=condition.getWithdrawalsNo();
			if (StringUtils.isNotBlank(withdrawalsNo)) {
				logger.info(log+"--提现失败 提现单号为空");
				throw new BusinessException(BusinessCode.CODE_600005);
			}
			map.put("withdrawalsNo",withdrawalsNo);
			List<PayStoreBankrollLog> list=payStoreBankrollLogMapper.selectListByNoAndType(map);
			if (CollectionUtils.isNotEmpty(list)) {
				//说明已经做过此操作
				logger.info(log+"--提现失败计算用户资金重复--提现单号"+withdrawalsNo);
				throw new BusinessException(BusinessCode.CODE_600013);
			}
			changeCondition=new StoreBankrollChangeCondition();
			changeCondition.setType(condition.getType());
			changeCondition.setWithdrawalsNo(withdrawalsNo);
			//可提现金额增加
			changeCondition.setPresentedMoney(condition.getMoney());
			//冻结金额减少(因为用户资金变化都是add，这里需要传负数)
			changeCondition.setPresentedFrozenMoney(condition.getMoney().multiply(BigDecimal.valueOf(-1)));
			flag=true;
		}
		

		if(StoreBankRollOpearateEnums.WITHDRAWALS_SUCCESS.getCode().equals(condition.getType())){
			//todo 验证该订单是否做过此项操作
			String withdrawalsNo=condition.getWithdrawalsNo();
			if (StringUtils.isNotBlank(withdrawalsNo)) {
				logger.info(log+"--提现单号为空");
				throw new BusinessException(BusinessCode.CODE_600005);
			}
			map.put("withdrawalsNo",withdrawalsNo);
			List<PayStoreBankrollLog> list=payStoreBankrollLogMapper.selectListByNoAndType(map);
			if (CollectionUtils.isNotEmpty(list)) {
				//说明在提现审核的时候  已经给用户添加过资金
				logger.info(log+"--提现审核计算用户资金重复--提现单号"+withdrawalsNo);
				throw new BusinessException(BusinessCode.CODE_600008);
			}
			 changeCondition=new StoreBankrollChangeCondition();
			 changeCondition.setType(condition.getType());
			 changeCondition.setWithdrawalsNo(withdrawalsNo);
			 //冻结金额减少(因为用户资金变化都是add，这里需要传负数)
			 changeCondition.setPresentedFrozenMoney(condition.getMoney().multiply(BigDecimal.valueOf(-1)));
			 //已提现金额增加
			 changeCondition.setAlreadyPresentedMoney(condition.getMoney());
			 flag=true;
			
		}
		if (flag) {
			storeBankrollChange(changeCondition);
		}
		
	}
	
	public void storeBankrollChange(StoreBankrollChangeCondition condition) {
		String lockKey = CacheName.CACHE_KEY_STORE_PICK_UP_CODE_GENERATE + condition.getStoreId();
		Lock lock = new RedisLock(cache, lockKey, BACKROLL_LOCK_EXPIRES_TIME);
		try{
			lock.lock();
			StoreBankroll storeBankroll=storeBankrollMapper.selectStoreBankrollByStoreId(condition.getStoreId());
			BigDecimal presentedFrozenMoney=condition.getPresentedFrozenMoney()==null?BigDecimal.valueOf(0):condition.getPresentedFrozenMoney();
			BigDecimal presentedMoney=condition.getPresentedMoney()==null?BigDecimal.valueOf(0):condition.getPresentedMoney();
			BigDecimal settlementSettledMoney=condition.getSettlementSettledMoney()==null?BigDecimal.valueOf(0):condition.getSettlementSettledMoney();
			BigDecimal alreadyPresentedMoney=condition.getAlreadyPresentedMoney()==null?BigDecimal.valueOf(0):condition.getAlreadyPresentedMoney();
			
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
				alreadyPresentedMoney=storeBankroll.getAlreadyPresentedMoney().add(alreadyPresentedMoney);
				settlementSettledMoney=storeBankroll.getSettlementSettledMoney().add(settlementSettledMoney);
				
				totalMoney=totalMoney.compareTo(BigDecimal.valueOf(0))<0?BigDecimal.valueOf(0):totalMoney;
				presentedFrozenMoney=presentedFrozenMoney.compareTo(BigDecimal.valueOf(0))<0?BigDecimal.valueOf(0):presentedFrozenMoney;
				presentedMoney=presentedMoney.compareTo(BigDecimal.valueOf(0))<0?BigDecimal.valueOf(0):presentedMoney;
				alreadyPresentedMoney=alreadyPresentedMoney.compareTo(BigDecimal.valueOf(0))<0?BigDecimal.valueOf(0):alreadyPresentedMoney;
				settlementSettledMoney=settlementSettledMoney.compareTo(BigDecimal.valueOf(0))<0?BigDecimal.valueOf(0):settlementSettledMoney;
				
				storeBankroll.setTotalMoeny(totalMoney);
				storeBankroll.setPresentedFrozenMoney(presentedFrozenMoney);
				storeBankroll.setPresentedMoney(presentedMoney);
				storeBankroll.setSettlementSettledMoney(settlementSettledMoney);
				storeBankroll.setAlreadyPresentedMoney(alreadyPresentedMoney);
				storeBankrollMapper.updateByPrimaryKeySelective(storeBankroll);
			}
		}finally{
			lock.unlock();
		}
		
		//加入资金流转日志
		saveStoreBankRollLog(condition);
		
	}

	public void saveStoreBankRollLog(StoreBankrollChangeCondition condition) {
		
		PayStoreBankrollLog payStoreBankrollLog = new PayStoreBankrollLog();
		BigDecimal presentedMoney=condition.getPresentedMoney();
		BigDecimal presentedFrozenMoney=condition.getPresentedFrozenMoney();
		BigDecimal settlementSettledMoney=condition.getSettlementSettledMoney();
		BigDecimal alreadyPresentedMoney=condition.getAlreadyPresentedMoney();
		String remarks="";
		if(StoreBankRollOpearateEnums.ORDER_FINISH.getCode().equals(condition.getType())){
			 remarks = "订单完成:总收入增加"+settlementSettledMoney +"元,待结算金额增加"+settlementSettledMoney+"元";
		}

		if(StoreBankRollOpearateEnums.SETTLEMENT_AUDIT.getCode().equals(condition.getType())){
			 remarks = "结算审核：待结算减少"+settlementSettledMoney +"元,可提现金额增加"+presentedMoney+"元";
		}

		if(StoreBankRollOpearateEnums.WITHDRAWALS_APPLY.getCode().equals(condition.getType())){
			 remarks = "提现申请:可提现金额减少"+presentedMoney +"元,提现冻结金额增加"+presentedFrozenMoney+"元";
		}

		if(StoreBankRollOpearateEnums.WITHDRAWALS_SUCCESS.getCode().equals(condition.getType())){
			 remarks = "提现成功:提现冻结金额减少"+presentedFrozenMoney +"元,已提现金额增加"+alreadyPresentedMoney+"元";
		}

		if(StoreBankRollOpearateEnums.WITHDRAWALS_AUDIT_NOT_PASS.getCode().equals(condition.getType())){
			remarks = "提现审核不通过:提现冻结金额减少"+presentedFrozenMoney +"元,可提现提现金额增加"+presentedMoney+"元";
		}
		
		if(StoreBankRollOpearateEnums.WITHDRAWALS_FAIL.getCode().equals(condition.getType())){
			remarks = "提现失败:提现冻结金额减少"+presentedFrozenMoney +"元,可提现金额增加"+presentedMoney+"元";
		}
		payStoreBankrollLog.setOrderNo(condition.getOrderNo());
		payStoreBankrollLog.setStoreId(condition.getStoreId());
		payStoreBankrollLog.setTotalMoeny(settlementSettledMoney);
		payStoreBankrollLog.setPresentedMoney(presentedMoney);
		payStoreBankrollLog.setSettlementSettledMoney(settlementSettledMoney);
		payStoreBankrollLog.setPresentedFrozenMoney(presentedFrozenMoney);
		payStoreBankrollLog.setAlreadyPresentedMoney(alreadyPresentedMoney);
		payStoreBankrollLog.setWithdrawalsNo(condition.getWithdrawalsNo());
		payStoreBankrollLog.setRemarks(remarks);
		payStoreBankrollLog.setMoneyType(condition.getMoneyType());
		payStoreBankrollLogMapper.insertSelective(payStoreBankrollLog);
	}

	@Override
	public PayPreOrderVO unifiedOrder(PayPreOrderCondition condition) {
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
		String payType=condition.getPayType();
		BigDecimal totalAmount=condition.getTotalAmount();
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
		if (StringUtils.isBlank(payType)) {
			logger.info(log+"--支付方式为空");
			throw new BusinessException(BusinessCode.CODE_600106);
		}
		if (StringUtils.isBlank(payType)) {
			logger.info(log+"--支付金额为空");
			throw new BusinessException(BusinessCode.CODE_600106);
		}
		logger.info(log+"--参数"+condition.toString());
		PayPreOrderVO payPreOrderVO=unifiedOrderService.unifiedOrder(condition);
		if (payPreOrderVO!=null) {
			//插入流水数据
			PayOrderPayment payOrderPayment=new PayOrderPayment();
			payOrderPayment.setOrderNo(orderNo);
			payOrderPayment.setOrderTransactionNo(payPreOrderVO.getOutTradeNo());
			payOrderPayment.setCreated(new Date());
			payOrderPayment.setBuyerId(openid);
			payOrderPayment.setRealPaymentMoney(totalAmount);
			payOrderPayment.setCallbackStatus(PayStatusEnums.PAYING.getCode());
			payOrderPaymentMapper.insertSelective(payOrderPayment);
			
		}
		
		return payPreOrderVO;
	}
    /**
     * 
     * @author liuhanning
     * @date  2018年8月20日 下午4:37:38
     * @Description 采用监听事件的方式  处理退款
     * @param orderNo
     * @param order
     * @return
     */
	@EventMessageListener(value = EventTypeHandler.EVENT_CUSTOMER_ORDER_REFUND_HANDLER)
	public PayRefundVO refundOrder(String orderNo, OrderInfo order)  {
		
		//验证订单支付参数
		String log=logLabel+"订单退款refundOrder";
		logger.info(log+"--开始");
		if (order==null){
			logger.info(log+"--参数为空");
			throw new BusinessException(BusinessCode.CODE_600201);
		}
		if (StringUtils.isBlank(orderNo)) {
			logger.info(log+"--订单号为空");
			throw new BusinessException(BusinessCode.CODE_600202);
		}
		
		if (order.getPayStatus() != PayStatusEnum.PAID.getStatusCode() || !StringUtils.isNotBlank(order.getPaymentSerialNum())) {
            throw new BusinessException(BusinessCode.ORDER_REFUND_STATUS_ERROR, "申请退款状态异常");
        }
		BigDecimal totalAmount=order.getRealPaymentMoney();
		BigDecimal refundAmount=order.getRealPaymentMoney();
		Long createdBy=order.getUpdatedBy();
		String createdByName=order.getUpdatedByName();
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
		logger.info(log+"--参数"+order.toString());
		PayRefundCondition payRefund = new PayRefundCondition();
		payRefund.setOutTradeNo(order.getPaymentSerialNum());
		payRefund.setTotalAmount(order.getRealPaymentMoney());
		payRefund.setRefundAmount(order.getRealPaymentMoney());
		payRefund.setCreatedBy(order.getUpdatedBy());
		payRefund.setCreatedByName(order.getUpdatedByName());
		payRefund.setRefundDesc(order.getCancelReason());
		PayRefundVO vo=refundService.refundOrder(payRefund);
		if (vo!=null) {
			//插入退款流水信息
			PayRefundPayment payRefundPayment=new PayRefundPayment();
			payRefundPayment.setOrderNo(payRefund.getOrderNo());
			payRefundPayment.setOrderTransactionNo(payRefund.getOutTradeNo());
			payRefundPayment.setRefundNo(payRefund.getOrderNo());
			payRefundPayment.setRefundTransactionNo(vo.getOutRefundNo());
			payRefundPayment.setRefundFee(payRefund.getRefundAmount());
			payRefundPayment.setTotalAmount(payRefund.getTotalAmount());
			payRefundPayment.setCreated(new Date());
			payRefundPayment.setRefundDesc(payRefund.getRefundDesc());
			payRefundPayment.setCallbackStatus(PayRefundStatusEnums.REFUNDING.getCode());
			payRefundPaymentMapper.insertSelective(payRefundPayment);
			if (vo.isStatus()) {
				//todo 更新订单事件
				logger.info("订单退款发送更新订单事件开始-订单号={}", order.getOrderNo());
		        eventMessageSender.send(EventType.EVENT_CUSTOMER_ORDER_REFUND_UPDATE_ORDER, order.getOrderNo(), order);
		        logger.info("订单退款发送更新订单事件结束-订单号={}", order.getOrderNo());
			}
		}
		return vo;
	}

	@Override
	public int transfersToChange(PayTransfersToWxChangeCondition toWxBalanceCondition) {
		String  log =logLabel + "微信提现至余额transfersToChange";
		logger.info(log+"--开始");
		if (toWxBalanceCondition==null) {
			logger.info(log+"--参数为空");
			throw new BusinessException(BusinessCode.CODE_600101);
		}
		if (StringUtils.isBlank(toWxBalanceCondition.getPartnerTradeNo())) {
			logger.info(log+"--提现流水号号为空");
			throw new BusinessException(BusinessCode.CODE_600005);
		}
		PayTransfersToWxChangeVO payTransfersToWxChangeVO = transfersService.transfersToChange(toWxBalanceCondition);

		if(null == payTransfersToWxChangeVO){
			logger.info(log+"--transfersService.transfersToChange返回结果为空");
		}
        PayWithdrawals payWithdrawals = new PayWithdrawals();
        payWithdrawals.setWithdrawalsNo(payTransfersToWxChangeVO.getPartnerTradeNo());
        if(payTransfersToWxChangeVO.isTransfersResult()){
            payWithdrawals.setCallbackStatus(WithdrawalsStatusEnum.SUCCESS.getStatusCode());
        }else{
            //是否需要用户重新申请提现流程
            if(payTransfersToWxChangeVO.isAbleContinue()){
                payWithdrawals.setCallbackStatus(WithdrawalsStatusEnum.FAIL.getStatusCode());
            }else{
                payWithdrawals.setCallbackStatus(WithdrawalsStatusEnum.REAPPLY.getStatusCode());
            }
        }
        payWithdrawals.setCallbackReason(payTransfersToWxChangeVO.getErrorDesc());
        payWithdrawals.setTransactionId(payTransfersToWxChangeVO.getPaymentNo());
        payWithdrawals.setTimeEnd(new Date());
        int transfersResult = this.transfersPublic(payWithdrawals,log);

		return transfersResult;
	}

	public int transfersPublic(PayWithdrawals payWithdrawals,String log){
		List<PayWithdrawals> payWithdrawalsList = payWithdrawalsMapper.selectByWithdrawalsNo(payWithdrawals.getWithdrawalsNo());
		if(CollectionUtils.isEmpty(payWithdrawalsList)){
			logger.info(log+"--提现记录不存在");
			throw new BusinessException(BusinessCode.CODE_600010);
		}

        //step 1  修改提现申请状态
        int payWithdrawalsResult  = payWithdrawalsMapper.updateByWithdrawalsNoSelective(payWithdrawals);

        //step 2出账明细表 pay_finance_account_detail
        PayFinanceAccountDetail payFinanceAccountDetail = new PayFinanceAccountDetail();
        payFinanceAccountDetail.setOrderNo(payWithdrawals.getWithdrawalsNo());
        payFinanceAccountDetail.setOutType(PayOutTypeEnum.STORE_WITHDRAW.getStatusCode());
        payFinanceAccountDetail.setStatus((short) 1);
        payFinanceAccountDetail.setCreated(new Date());
        payFinanceAccountDetail.setTradeNo(payWithdrawals.getWithdrawalsNo());
        int payFinanceInsertResult = payFinanceAccountDetailService.saveFinanceAccountDetail(payFinanceAccountDetail);
        if (payFinanceInsertResult<1) {
            logger.info(log+"--订单出账明细表插入失败");
        }
        //step 3保存交易记录
        PayStoreTransactionRecord payStoreTransactionRecord = new PayStoreTransactionRecord();
		payStoreTransactionRecord.setOrderNo(payWithdrawals.getWithdrawalsNo());
		payStoreTransactionRecord.setType(StoreTransactionStatusEnum.TRANSFERS.getStatusCode());
		payStoreTransactionRecord.setStatus((short) 1);
        payStoreTransactionRecord.setStoreId(payWithdrawalsList.get(0).getStoreId());
		payStoreTransactionRecord.setMoney(payWithdrawalsList.get(0).getTotalFee());
		payStoreTransactionRecord.setCmmsAmt(payWithdrawalsList.get(0).getCmmsAmt());
		payStoreTransactionRecord.setTransactionDate(payWithdrawalsList.get(0).getCreated());
        payStoreCashService.savePayStoreTransactionRecord(payStoreTransactionRecord);
        //step4 门店资金变化
		UpdateStoreBankRollCondition updateStoreBankRollCondition = new UpdateStoreBankRollCondition();
		if(WithdrawalsStatusEnum.SUCCESS.getStatusCode() == payWithdrawals.getCallbackStatus()){
			updateStoreBankRollCondition.setType(StoreBankRollOpearateEnums.WITHDRAWALS_SUCCESS.getCode());
		}
		if(WithdrawalsStatusEnum.REAPPLY.getStatusCode() == payWithdrawals.getCallbackStatus()){
			updateStoreBankRollCondition.setType(StoreBankRollOpearateEnums.WITHDRAWALS_FAIL.getCode());
		}
		updateStoreBankRollCondition.setStoreId(payWithdrawalsList.get(0).getStoreId());
		updateStoreBankRollCondition.setWithdrawalsNo(payWithdrawalsList.get(0).getWithdrawalsNo());
		updateStoreBankRollCondition.setMoney(payWithdrawalsList.get(0).getTotalFee());
		this.updateStoreBankroll(updateStoreBankRollCondition);

        return payWithdrawalsResult ;
    }

	@Override
	public int transfersToBank(PayTransfersToWxBankCondition toWxBankCondition) {
		String  log =logLabel + "微信提现至银行卡transfersToBank";
		logger.info(log+"--开始");
		if (toWxBankCondition==null) {
			logger.info(log+"--参数为空");
			throw new BusinessException(BusinessCode.CODE_600101);
		}
		if (StringUtils.isBlank(toWxBankCondition.getPartnerTradeNo())) {
			logger.info(log+"--提现流水号号为空");
			throw new BusinessException(BusinessCode.CODE_600310);
		}
		PayTransfersToWxBankVO payTransfersToWxBankVO = transfersService.transfersToBank(toWxBankCondition);
        if(null == payTransfersToWxBankVO){
            logger.info(log+"--transfersService.transfersToChange返回结果为空");
        }
        PayWithdrawals payWithdrawals = new PayWithdrawals();
        payWithdrawals.setWithdrawalsNo(payTransfersToWxBankVO.getPartnerTradeNo());
		if(payTransfersToWxBankVO.isTransfersResult()){
			payWithdrawals.setCallbackStatus(WithdrawalsStatusEnum.SUCCESS.getStatusCode());
		}else{
			//是否需要用户重新申请提现流程
			if(payTransfersToWxBankVO.isAbleContinue()){
				payWithdrawals.setCallbackStatus(WithdrawalsStatusEnum.FAIL.getStatusCode());
			}else{
				payWithdrawals.setCallbackStatus(WithdrawalsStatusEnum.REAPPLY.getStatusCode());
			}
		}
        payWithdrawals.setCallbackReason(payTransfersToWxBankVO.getErrorDesc());
        payWithdrawals.setTransactionId(payTransfersToWxBankVO.getPaymentNo());
        payWithdrawals.setTimeEnd(new Date());
        int transfersResult = this.transfersPublic(payWithdrawals,log);


		return transfersResult;
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
	public Boolean orderIsPay(OrderIsPayCondition condition) {
		String log=logLabel+"查询订单是否支付过orderIsPay";
		logger.info(log+"--开始");
		if (condition==null||StringUtils.isBlank(condition.getOrderNo())) {
			logger.info(log+"--订单号为空");
			throw new BusinessException(BusinessCode.CODE_601601);
		}
		Boolean isPay=false;
		List<PayOrderPayment> list=payOrderPaymentMapper.selectByOrderNo(condition.getOrderNo());
		if (CollectionUtils.isNotEmpty(list)) {
			//已经有过支付流水
			isPay=true;
		}
		return isPay;
	}
}
