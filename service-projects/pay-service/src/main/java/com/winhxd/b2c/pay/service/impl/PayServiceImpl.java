package com.winhxd.b2c.pay.service.impl;

import com.winhxd.b2c.common.cache.Cache;
import com.winhxd.b2c.common.cache.Lock;
import com.winhxd.b2c.common.cache.RedisLock;
import com.winhxd.b2c.common.constant.*;
import com.winhxd.b2c.common.context.StoreUser;
import com.winhxd.b2c.common.context.UserContext;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.message.enums.MsgCategoryEnum;
import com.winhxd.b2c.common.domain.order.condition.OrderRefundCallbackCondition;
import com.winhxd.b2c.common.domain.order.condition.OrderRefundFailCondition;
import com.winhxd.b2c.common.domain.order.enums.PayStatusEnum;
import com.winhxd.b2c.common.domain.order.model.OrderInfo;
import com.winhxd.b2c.common.domain.order.vo.OrderInfoDetailVO;
import com.winhxd.b2c.common.domain.order.vo.OrderInfoDetailVO4Management;
import com.winhxd.b2c.common.domain.pay.condition.*;
import com.winhxd.b2c.common.domain.pay.constant.WXCalculation;
import com.winhxd.b2c.common.domain.pay.enums.*;
import com.winhxd.b2c.common.domain.pay.model.*;
import com.winhxd.b2c.common.domain.pay.vo.*;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.order.OrderServiceClient;
import com.winhxd.b2c.common.util.MessageSendUtils;
import com.winhxd.b2c.pay.dao.*;
import com.winhxd.b2c.pay.service.PayFinanceAccountDetailService;
import com.winhxd.b2c.pay.service.PayService;
import com.winhxd.b2c.pay.service.PayStoreCashService;
import com.winhxd.b2c.pay.util.PayUtil;
import com.winhxd.b2c.pay.weixin.constant.PayTransfersStatus;
import com.winhxd.b2c.pay.weixin.model.PayBill;
import com.winhxd.b2c.pay.weixin.model.PayRefund;
import com.winhxd.b2c.pay.weixin.service.WXRefundService;
import com.winhxd.b2c.pay.weixin.service.WXTransfersService;
import com.winhxd.b2c.pay.weixin.service.WXUnifiedOrderService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

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
	private WXTransfersService wxTransfersService;
	@Autowired
	private MessageSendUtils messageServiceClient;
	
	@Autowired
	private AccountingDetailMapper accountingDetailMapper;

	@Autowired
    private Cache cache;
    private static final int BACKROLL_LOCK_EXPIRES_TIME = 3000;

	
	private static final String logLabel="PayServiceImpl--";

	@Override
	@Transactional
	public Boolean callbackOrderPay(PayBill condition) {
		String log=logLabel+"支付回调callbackOrderPay";
		if (condition==null) {
			logger.info(log+"--参数为空");
//			throw new BusinessException(BusinessCode.CODE_600101);
			return false;
		}
		if (StringUtils.isBlank(condition.getOutOrderNo())) {
			logger.info(log+"--订单号为空");
//			throw new BusinessException(BusinessCode.CODE_600004);
			return false;
		}
		log+="--订单号--"+condition.getOutOrderNo();
		logger.info(log+"--支付开始");
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
//					throw new BusinessException(BusinessCode.CODE_600301);
		}
		//判断支付成功之后更新订单信息
		if(PayStatusEnums.PAY_SUCCESS.getCode().equals(condition.getStatus())){
			// 判断订单是否更新成功
			try {
				ResponseResult<Void> orderResult=	orderServiceClient.orderPaySuccessNotify(condition.getOutOrderNo(),condition.getOutTradeNo());
				if (orderResult==null) {
					logger.info(log+"订单更新返回结果为空 ");
					return false;
				}
				if (orderResult.getCode()==BusinessCode.ORDER_ALREADY_PAID) {
					return true;
				}
			}catch(BusinessException e){
				if (BusinessCode.ORDER_ALREADY_PAID==e.getErrorCode()) {
					return true;
				}else {
					logger.error(log+"BusinessException订单更新失败",e);
					return false;
				}
			}catch (RuntimeException e) {
				Throwable ex=e.getCause();
				if (ex == null) {
					logger.error(log+"Throwable订单更新失败",e);
		            return false;
		        }
		        if (ex instanceof BusinessException) {
		            BusinessException businessException=(BusinessException)ex;
		            if (BusinessCode.ORDER_ALREADY_PAID==businessException.getErrorCode()) {
						return true;
					}else {
						logger.error(log+"Throwable--BusinessException订单更新失败",e);
						return false;
					}
		        }
			} catch (Exception e) {
				logger.error(log+"订单更新失败",e);
				return false;
			}
			
			
		}

		logger.info(log+"--支付结束");
		return true;
	}


	@Override
	@Transactional
	public Boolean callbackOrderRefund(PayRefund condition) {
		String log=logLabel+"退款回调callbackOrderRefund";
		if (condition==null) {
			logger.info(log+"--参数为空");
//			throw new BusinessException(BusinessCode.CODE_600101);
			return false;
		}
		if (StringUtils.isBlank(condition.getOrderNo())) {
			logger.info(log+"--订单号为空");
//			throw new BusinessException(BusinessCode.CODE_600004);
			return false;
		}
        log+="--订单号--"+condition.getOrderNo();
        logger.info(log+"--开始退款回调");
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
			//出账明细表 pay_finance_account_detail
			PayFinanceAccountDetail payFinanceAccountDetail = new PayFinanceAccountDetail();
			payFinanceAccountDetail.setOrderNo(condition.getOrderNo());
			payFinanceAccountDetail.setOutType(PayOutTypeEnum.CUSTOMER_REFUND.getStatusCode());
			payFinanceAccountDetail.setStatus(StatusEnums.EFFECTIVE.getCode());
			payFinanceAccountDetail.setCreated(new Date());
			payFinanceAccountDetail.setTradeNo(condition.getOutRefundNo());
			int payFinanceInsertResult = payFinanceAccountDetailService.saveFinanceAccountDetail(payFinanceAccountDetail);
			if (payFinanceInsertResult<1) {
				logger.info(log+"--订单出账明细表插入失败");
			}
			//更新订单状态
			OrderRefundCallbackCondition orderRefundCallbackCondition=new OrderRefundCallbackCondition();
			orderRefundCallbackCondition.setOrderNo(condition.getOrderNo());
			try {
				ResponseResult<Boolean> callbackResult=orderServiceClient.updateOrderRefundCallback(orderRefundCallbackCondition);
				if (callbackResult.getCode()!=BusinessCode.CODE_OK&&!callbackResult.getData()) {
					//订单更新失败
					logger.info(log+"--订单更新失败");
					return false;
				}
				logger.info(log+"--订单更新状态"+callbackResult.getData());
			} catch (Exception e) {
				logger.error(log+"--订单更新失败",e);
				return false;
			}
		}
		logger.info(log+"--结束退款回调");
		return true;
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
		if (condition.getMoney().compareTo(BigDecimal.valueOf(0))<=0) {
			logger.info(log+"--金额有误");
			throw new BusinessException(BusinessCode.CODE_600010);
		}
		logger.info(log+"--参数"+condition.toString());
		boolean flag=false;
		Map<String, Object> map=new HashMap<>();
		map.put("type", condition.getType());
		map.put("storeId", storeId);
		StoreBankrollChangeCondition changeCondition=new StoreBankrollChangeCondition();
	    changeCondition.setStoreId(storeId);
	    changeCondition.setType(condition.getType());
	    //订单闭环
		if(StoreBankRollOpearateEnums.ORDER_FINISH.getCode().equals(condition.getType())){
			//由于订单闭环是由  监听事件触发，不适合抛异常
			// 验证该订单是否已经做过此项操作
			String orderNo=condition.getOrderNo();
			if (StringUtils.isBlank(orderNo)) {
				logger.info(log+"--订单闭环订单号为空");
				return;
			}
			//验证订单是否有效
			//验证该订单是否做过此项操作
			map.put("orderNo",orderNo);
			List<PayStoreBankrollLog> list=payStoreBankrollLogMapper.selectListByNoAndType(map);
			if (CollectionUtils.isNotEmpty(list)) {
				//说明在订单闭环的时候  已经给用户添加过资金
				logger.info(log+"--订单闭环计算用户资金重复--订单号"+orderNo);
				return;
			}
			 changeCondition.setOrderNo(orderNo);
			 //待结算金额增加
			 changeCondition.setSettlementSettledMoney(condition.getMoney());
			 flag=true;
		}
		//审核结算
		if(StoreBankRollOpearateEnums.SETTLEMENT_AUDIT.getCode().equals(condition.getType())){
          //验证订单信息
			String orderNo=condition.getOrderNo();
			Short moneyType=condition.getMoneyType();
			if (StringUtils.isBlank(orderNo)) {
				logger.info(log+"--结算审核订单号为空");
				throw new BusinessException(BusinessCode.CODE_600004);
			}
			if (moneyType==null) {
				logger.info(log+"--结算审核费用类型为空");
				throw new BusinessException(BusinessCode.CODE_600007);
			}
			//验证结算数据
			List<AccountingDetail>  accountingDetails=accountingDetailMapper.selectAccountingDetailListByOrderNo(orderNo);
			if (CollectionUtils.isEmpty(accountingDetails)) {
				logger.info(log+"--结算审核数据无效");
				throw new BusinessException(BusinessCode.CODE_600008);
			}
			//验证该订单是否做过此项操作
			map.put("orderNo",orderNo);
			map.put("moneyType", moneyType);
			List<PayStoreBankrollLog> list=payStoreBankrollLogMapper.selectListByNoAndType(map);
			if (CollectionUtils.isNotEmpty(list)) {
				//说明在订单闭环的时候  已经给用户添加过资金
				logger.info(log+"--结算审核计算用户资金重复--订单号"+orderNo+"费用类型"+moneyType);
				throw new BusinessException(BusinessCode.CODE_600006);
			}
			 changeCondition.setOrderNo(orderNo);
			 changeCondition.setMoneyType(moneyType);
			 //待结算金额减少(因为用户资金变化都是add，这里需要传负数)
			 changeCondition.setSettlementSettledMoney(condition.getMoney().multiply(BigDecimal.valueOf(-1)));
			 //可提现金额增加
			 changeCondition.setPresentedMoney(condition.getMoney());
			 flag=true;
		
			
		}
        //提现申请
		if(StoreBankRollOpearateEnums.WITHDRAWALS_APPLY.getCode().equals(condition.getType())){
			//todo 验证该订单是否做过此项操作
			String withdrawalsNo=condition.getWithdrawalsNo();
			
			storeBankrollVerifyWithdrawalsNo(log+"提现申请",withdrawalsNo,map);
			 
			 changeCondition.setWithdrawalsNo(withdrawalsNo);
			 //可提现金额减少(因为用户资金变化都是add，这里需要传负数)
			 changeCondition.setPresentedMoney(condition.getMoney().multiply(BigDecimal.valueOf(-1)));
			 //冻结金额增加
			 changeCondition.setPresentedFrozenMoney(condition.getMoney());
			 flag=true;
			
		}
		//提现审核不通过
		if(StoreBankRollOpearateEnums.WITHDRAWALS_AUDIT_NOT_PASS.getCode().equals(condition.getType())){
			// 验证该订单是否做过此项操作
			String withdrawalsNo=condition.getWithdrawalsNo();
			
			storeBankrollVerifyWithdrawalsNo(log+"提现审核不通过",withdrawalsNo,map);
			
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
			
			storeBankrollVerifyWithdrawalsNo(log+"提现失败",withdrawalsNo,map);
			
			changeCondition.setWithdrawalsNo(withdrawalsNo);
			//可提现金额增加
			changeCondition.setPresentedMoney(condition.getMoney());
			//冻结金额减少(因为用户资金变化都是add，这里需要传负数)
			changeCondition.setPresentedFrozenMoney(condition.getMoney().multiply(BigDecimal.valueOf(-1)));
			flag=true;
		}
		
        //
		if(StoreBankRollOpearateEnums.WITHDRAWALS_SUCCESS.getCode().equals(condition.getType())){
			// 验证该订单是否做过此项操作
			String withdrawalsNo=condition.getWithdrawalsNo();
			
			storeBankrollVerifyWithdrawalsNo(log+"提现成功",withdrawalsNo,map);
			
			 changeCondition.setWithdrawalsNo(withdrawalsNo);
			 //冻结金额减少(因为用户资金变化都是add，这里需要传负数)
			 changeCondition.setPresentedFrozenMoney(condition.getMoney().multiply(BigDecimal.valueOf(-1)));
			 //已提现金额增加
			 changeCondition.setAlreadyPresentedMoney(condition.getMoney());
			 flag=true;
			
		}
		//银行退票
		if(StoreBankRollOpearateEnums.BANK_FAIL.getCode().equals(condition.getType())){
			// 验证该订单是否做过此项操作
			String withdrawalsNo=condition.getWithdrawalsNo();
			
			storeBankrollVerifyWithdrawalsNo(log+"银行退票",withdrawalsNo,map);
			
			changeCondition.setWithdrawalsNo(withdrawalsNo);
			//可提现金额增加
			changeCondition.setPresentedMoney(condition.getMoney());
			//已提现金额减少(因为用户资金变化都是add，这里需要传负数)
			changeCondition.setAlreadyPresentedMoney(condition.getMoney().multiply(BigDecimal.valueOf(-1)));
			flag=true;
			
		}
		if (flag) {
			changeCondition.setMoney(condition.getMoney());
			storeBankrollChange(changeCondition);
		}
		
	}
	
	public void storeBankrollVerifyWithdrawalsNo(String log,String withdrawalsNo,Map<String, Object> map) {
		if (StringUtils.isBlank(withdrawalsNo)) {
			logger.info(log+"--提现单号为空");
			throw new BusinessException(BusinessCode.CODE_600005);
		}
		//验证提现数据是否有效
		List<PayWithdrawals> withdrawals=payWithdrawalsMapper.selectByWithdrawalsNo(withdrawalsNo);
		if (CollectionUtils.isEmpty(withdrawals)) {
			logger.info(log+"--提现数据无效");
			throw new BusinessException(BusinessCode.CODE_600011);
		}
		map.put("withdrawalsNo",withdrawalsNo);
		List<PayStoreBankrollLog> list=payStoreBankrollLogMapper.selectListByNoAndType(map);
		if (CollectionUtils.isNotEmpty(list)) {
			//请勿重复给用户变化资金
			logger.info(log+"--计算用户资金重复--提现单号"+withdrawalsNo);
			throw new BusinessException(BusinessCode.CODE_600006);
		}
	}
	
	public void storeBankrollChange(StoreBankrollChangeCondition condition) {
		String lockKey = CacheName.BACKROLL_STORE + condition.getStoreId();
		logger.info(logLabel+"门店资金变化参数--"+condition.toString());
		Lock lock = new RedisLock(cache, lockKey, BACKROLL_LOCK_EXPIRES_TIME);
		try{
			lock.lock();
			StoreBankroll storeBankroll=storeBankrollMapper.selectStoreBankrollByStoreId(condition.getStoreId());
			BigDecimal presentedFrozenMoney=condition.getPresentedFrozenMoney()==null?BigDecimal.valueOf(0):condition.getPresentedFrozenMoney();
			BigDecimal presentedMoney=condition.getPresentedMoney()==null?BigDecimal.valueOf(0):condition.getPresentedMoney();
			BigDecimal settlementSettledMoney=condition.getSettlementSettledMoney()==null?BigDecimal.valueOf(0):condition.getSettlementSettledMoney();
			BigDecimal alreadyPresentedMoney=condition.getAlreadyPresentedMoney()==null?BigDecimal.valueOf(0):condition.getAlreadyPresentedMoney();
			
//			presentedFrozenMoney=presentedFrozenMoney.compareTo(BigDecimal.valueOf(0))<0?BigDecimal.valueOf(0):presentedFrozenMoney;
//			presentedMoney=presentedMoney.compareTo(BigDecimal.valueOf(0))<0?BigDecimal.valueOf(0):presentedMoney;
//			alreadyPresentedMoney=alreadyPresentedMoney.compareTo(BigDecimal.valueOf(0))<0?BigDecimal.valueOf(0):alreadyPresentedMoney;
//			settlementSettledMoney=settlementSettledMoney.compareTo(BigDecimal.valueOf(0))<0?BigDecimal.valueOf(0):settlementSettledMoney;
			
			BigDecimal totalMoney=settlementSettledMoney;
			if (storeBankroll==null) {
				storeBankroll=new StoreBankroll();
				storeBankroll.setStoreId(condition.getStoreId());
				storeBankroll.setTotalMoeny(totalMoney);
				storeBankroll.setPresentedFrozenMoney(presentedFrozenMoney);
				storeBankroll.setPresentedMoney(presentedMoney);
				storeBankroll.setAlreadyPresentedMoney(alreadyPresentedMoney);
				storeBankroll.setCreated(new Date());
				storeBankroll.setSettlementSettledMoney(settlementSettledMoney);
				storeBankroll.setStatus(StatusEnums.EFFECTIVE.getCode());
				storeBankrollMapper.insertSelective(storeBankroll);
			}else {
				if (StoreBankRollOpearateEnums.ORDER_FINISH.getCode().equals(condition.getType())) {
					//只有订单闭环才增加总的收入
					totalMoney=totalMoney.compareTo(BigDecimal.valueOf(0))<0?BigDecimal.valueOf(0):totalMoney;
					totalMoney=storeBankroll.getTotalMoeny().add(totalMoney);
					storeBankroll.setTotalMoeny(totalMoney);
				}
				presentedFrozenMoney=storeBankroll.getPresentedFrozenMoney().add(presentedFrozenMoney);
				presentedMoney=storeBankroll.getPresentedMoney().add(presentedMoney);
				alreadyPresentedMoney=storeBankroll.getAlreadyPresentedMoney().add(alreadyPresentedMoney);
				settlementSettledMoney=storeBankroll.getSettlementSettledMoney().add(settlementSettledMoney);
				
				
				presentedFrozenMoney=presentedFrozenMoney.compareTo(BigDecimal.valueOf(0))<0?BigDecimal.valueOf(0):presentedFrozenMoney;
				presentedMoney=presentedMoney.compareTo(BigDecimal.valueOf(0))<0?BigDecimal.valueOf(0):presentedMoney;
				alreadyPresentedMoney=alreadyPresentedMoney.compareTo(BigDecimal.valueOf(0))<0?BigDecimal.valueOf(0):alreadyPresentedMoney;
				settlementSettledMoney=settlementSettledMoney.compareTo(BigDecimal.valueOf(0))<0?BigDecimal.valueOf(0):settlementSettledMoney;
				
				storeBankroll.setPresentedFrozenMoney(presentedFrozenMoney);
				storeBankroll.setPresentedMoney(presentedMoney);
				storeBankroll.setSettlementSettledMoney(settlementSettledMoney);
				storeBankroll.setAlreadyPresentedMoney(alreadyPresentedMoney);
				storeBankroll.setUpdated(new Date());
				storeBankrollMapper.updateByPrimaryKeySelective(storeBankroll);
			}
			//加入资金流转日志
			condition.setTotalMoeny(storeBankroll.getTotalMoeny());
			condition.setPresentedFrozenMoney(presentedFrozenMoney);
			condition.setPresentedMoney(presentedMoney);
			condition.setSettlementSettledMoney(settlementSettledMoney);
			condition.setAlreadyPresentedMoney(alreadyPresentedMoney);
			saveStoreBankRollLog(condition);
		}finally{
			lock.unlock();
		}
		
		
		
	}

	public void saveStoreBankRollLog(StoreBankrollChangeCondition condition) {
		
		PayStoreBankrollLog payStoreBankrollLog = new PayStoreBankrollLog();
		BigDecimal totalMoney=condition.getTotalMoeny();
		BigDecimal presentedMoney=condition.getPresentedMoney();
		BigDecimal presentedFrozenMoney=condition.getPresentedFrozenMoney();
		BigDecimal settlementSettledMoney=condition.getSettlementSettledMoney();
		BigDecimal alreadyPresentedMoney=condition.getAlreadyPresentedMoney();
		BigDecimal money=condition.getMoney();
		String remarks="";
		if(StoreBankRollOpearateEnums.ORDER_FINISH.getCode().equals(condition.getType())){
			
			 remarks = "订单完成:总收入增加"+money +"元,待结算金额增加"+money+"元";
		}

		if(StoreBankRollOpearateEnums.SETTLEMENT_AUDIT.getCode().equals(condition.getType())){
			 remarks = "结算审核：待结算减少"+money +"元,可提现金额增加"+money+"元";
		}

		if(StoreBankRollOpearateEnums.WITHDRAWALS_APPLY.getCode().equals(condition.getType())){
			 remarks = "提现申请:可提现金额减少"+money +"元,提现冻结金额增加"+money+"元";
		}

		if(StoreBankRollOpearateEnums.WITHDRAWALS_SUCCESS.getCode().equals(condition.getType())){
			 remarks = "提现成功:提现冻结金额减少"+money +"元,已提现金额增加"+money+"元";
		}

		if(StoreBankRollOpearateEnums.WITHDRAWALS_AUDIT_NOT_PASS.getCode().equals(condition.getType())){
			remarks = "提现审核不通过:提现冻结金额减少"+money +"元,可提现提现金额增加"+money+"元";
		}
		
		if(StoreBankRollOpearateEnums.WITHDRAWALS_FAIL.getCode().equals(condition.getType())){
			remarks = "提现失败:提现冻结金额减少"+money +"元,可提现金额增加"+money+"元";
		}
		if(StoreBankRollOpearateEnums.BANK_FAIL.getCode().equals(condition.getType())){
			remarks = "银行退票:已提现金额减少"+money +"元,可提现金额增加"+money+"元";
		}
		payStoreBankrollLog.setOrderNo(condition.getOrderNo());
		payStoreBankrollLog.setStoreId(condition.getStoreId());
		payStoreBankrollLog.setPresentedMoney(presentedMoney);
		payStoreBankrollLog.setTotalMoeny(totalMoney);
		payStoreBankrollLog.setSettlementSettledMoney(settlementSettledMoney);
		payStoreBankrollLog.setPresentedFrozenMoney(presentedFrozenMoney);
		payStoreBankrollLog.setAlreadyPresentedMoney(alreadyPresentedMoney);
		payStoreBankrollLog.setWithdrawalsNo(condition.getWithdrawalsNo());
		payStoreBankrollLog.setRemarks(remarks);
		payStoreBankrollLog.setMoneyType(condition.getMoneyType());
		payStoreBankrollLog.setType(condition.getType());
		payStoreBankrollLog.setCreated(new Date());
		payStoreBankrollLog.setUpdated(new Date());
		payStoreBankrollLog.setStatus(StatusEnums.EFFECTIVE.getCode());
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
		Short payType=condition.getPayType();
		BigDecimal totalAmount=condition.getTotalAmount();
		if (StringUtils.isBlank(orderNo)) {
			logger.info(log+"--订单号为空");
			throw new BusinessException(BusinessCode.CODE_600102);
		}
		log+="订单号--"+orderNo;
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
		if (payType==null) {
			logger.info(log+"--支付方式为空");
			throw new BusinessException(BusinessCode.CODE_600106);
		}
		if (totalAmount==null) {
			logger.info(log+"--支付金额为空");
			throw new BusinessException(BusinessCode.CODE_600106);
		}
		logger.info(log+"--参数"+condition.toString());
		PayPreOrderVO payPreOrderVO=unifiedOrderService.unifiedOrder(condition);
		OrderPayVO vo=new OrderPayVO();
		if (payPreOrderVO!=null) {
			//插入流水数据
			 String timeStamp=payPreOrderVO.getTimeStamp();
			 String nonceStr=payPreOrderVO.getNonceStr();
			 String packageData=payPreOrderVO.getPackageData();
			 String signType=payPreOrderVO.getSignType();
			 String paySign=payPreOrderVO.getPaySign();
			 String orderTransactionNo=payPreOrderVO.getOutTradeNo();
			 String appid=payPreOrderVO.getAppId();
				 
			 PayOrderPayment payOrderPayment=payOrderPaymentMapper.selectByOrderPaymentNo(orderTransactionNo);
			 if (payOrderPayment == null) {
				 payOrderPayment=new PayOrderPayment();
				 payOrderPayment.setOrderNo(orderNo);
				 payOrderPayment.setOrderTransactionNo(orderTransactionNo);
				 payOrderPayment.setCreated(new Date());
				 payOrderPayment.setBuyerId(openid);
				 payOrderPayment.setPayType(payType);
				 payOrderPayment.setRealPaymentMoney(totalAmount);
				 payOrderPayment.setCallbackStatus(PayStatusEnums.PAYING.getCode());
				 payOrderPayment.setAppid(appid);
				 payOrderPaymentMapper.insertSelective(payOrderPayment);
			}
			 
			vo.setNonceStr(nonceStr);
			vo.setPackageData(packageData);
			vo.setPaySign(paySign);
			vo.setSignType(signType);
			vo.setTimeStamp(timeStamp);
			vo.setAppid(appid);
		}
		logger.info(log+"--结束");
		return vo;
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
	@Override
	public void refundOrder(String orderNo, OrderInfo order)  {
		
		//验证订单支付参数
		String log=logLabel+"订单退款refundOrder--订单号"+orderNo;
		logger.info(log+"--开始");
		//查询订单
		ResponseResult<OrderInfoDetailVO4Management> orderResult=orderServiceClient.getOrderDetail4Management(orderNo);
		if (orderResult==null||orderResult.getData().getOrderInfoDetailVO()==null) {
			logger.info(log+"--未获取到订单数据");
			return;
		}
		OrderInfoDetailVO orderVO=orderResult.getData().getOrderInfoDetailVO();
		if (!orderVO.getPayStatus().equals( PayStatusEnum.PAID.getStatusCode()) || StringUtils.isBlank(order.getPaymentSerialNum())) {
			logger.info(log+"--订单状态错误");
			return;
        }
		BigDecimal totalAmount=orderVO.getRealPaymentMoney();
		BigDecimal refundAmount=orderVO.getRealPaymentMoney();
		Long createdBy=order.getUpdatedBy();
		String createdByName=order.getUpdatedByName();
		if (totalAmount==null) {
			logger.info(log+"--订单金额为空");
//			throw new BusinessException(BusinessCode.CODE_600204);
			return;
		}
		if (refundAmount==null) {
			logger.info(log+"--退款金额为空");
//			throw new BusinessException(BusinessCode.CODE_600205);
			return;
		}
		if (createdBy==null) {
			logger.info(log+"--创建人为空");
//			throw new BusinessException(BusinessCode.CODE_600206);
			return;
		}
		if (StringUtils.isBlank(createdByName)) {
			logger.info(log+"--创建人姓名为空");
//			throw new BusinessException(BusinessCode.CODE_600207);
			return;
		}
		logger.info(log+"--参数"+order.toString());
		PayRefundCondition payRefund = new PayRefundCondition();
		payRefund.setOutTradeNo(order.getPaymentSerialNum());
		payRefund.setOrderNo(orderNo);
		payRefund.setTotalAmount(totalAmount);
		payRefund.setRefundAmount(refundAmount);
		payRefund.setCreatedBy(order.getUpdatedBy());
		payRefund.setCreatedByName(order.getUpdatedByName());
		payRefund.setRefundDesc(order.getCancelReason());
		PayRefundVO vo=refundService.refundOrder(payRefund);
		if (vo!=null) {
			//插入退款流水信息
			//判断是否有退款流水记录，有就不插入（退款流水号用的是交易流水号）
			PayRefundPayment payRefundPayment=payRefundPaymentMapper.selectByRefundTransactionNo(order.getPaymentSerialNum());
			if (payRefundPayment==null) {
				payRefundPayment=new PayRefundPayment();
				payRefundPayment.setCreated(new Date());
				payRefundPayment.setOrderNo(orderNo);
				payRefundPayment.setOrderTransactionNo(payRefund.getOutTradeNo());
				payRefundPayment.setRefundNo(orderNo);
				payRefundPayment.setRefundTransactionNo(order.getPaymentSerialNum());
				payRefundPayment.setRefundFee(payRefund.getRefundAmount());
				payRefundPayment.setTotalAmount(payRefund.getTotalAmount());
				payRefundPayment.setRefundDesc(payRefund.getRefundDesc());
			}	
			payRefundPayment.setCallbackErrorCode(vo.getErrorCode());
			payRefundPayment.setCallbackErrorMessage(vo.getErrorCodeDesc());
			payRefundPayment.setUpdated(new Date());
			payRefundPayment.setCallbackStatus(PayRefundStatusEnums.REFUNDING.getCode());
			if(!vo.isStatus()){
				payRefundPayment.setCallbackStatus(PayRefundStatusEnums.REFUND_FAIL.getCode());
			}
			if(payRefundPayment.getId()!=null){
				logger.info(log+"--更新退款流水");
				payRefundPaymentMapper.updateByPrimaryKeySelective(payRefundPayment);
			}else{
				logger.info(log+"--插入退款流水");
				payRefundPaymentMapper.insertSelective(payRefundPayment);
			}
			if(!vo.isStatus()){
				logger.info(log+"--退款失败："+vo.getErrorCode()+vo.getErrorCodeDesc());
				PayRefundErrorEnum payRefundErrorEnum=PayRefundErrorEnum.getErrorMsgByErrorCode(vo.getErrorCode());
				String errorCodeDesc=vo.getErrorCodeDesc();
				OrderRefundFailCondition orderRefundFailCondition = new OrderRefundFailCondition();
				orderRefundFailCondition.setOrderNo(orderNo);
				orderRefundFailCondition.setCustomerFail(payRefundErrorEnum.getAbleContinue());
				orderRefundFailCondition.setRefundErrorCode(vo.getErrorCode());
				orderRefundFailCondition.setRefundErrorDesc(errorCodeDesc);

				orderServiceClient.updateOrderRefundFail(orderRefundFailCondition);
			}

		}
		logger.info(log+"--结束");
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

		List<PayWithdrawals> payWithdrawalsList = payWithdrawalsMapper.selectByWithdrawalsNo(toWxBalanceCondition.getPartnerTradeNo());
		if(CollectionUtils.isEmpty(payWithdrawalsList)){
			logger.info(log+"--提现记录不存在");
			throw new BusinessException(BusinessCode.CODE_600010);
		}
		PayTransfersToWxChangeVO payTransfersToWxChangeVO = transfersService.transfersToChange(toWxBalanceCondition);

		if(null == payTransfersToWxChangeVO){
			logger.error(log+"--transfersService.transfersToChange返回结果为空");
			throw new BusinessException(BusinessCode.CODE_610039);
		}
		PayWithdrawals payWithdrawals = payWithdrawalsList.get(0);
		logger.info("提现流水号111:{},对象信息：",payTransfersToWxChangeVO.toString());
		if(payTransfersToWxChangeVO.isTransfersResult()){
			payWithdrawals.setCallbackStatus(WithdrawalsStatusEnum.SUCCESS.getStatusCode());
		}else{
			//是否需要用户重新申请提现流程
			if(payTransfersToWxChangeVO.isAbleContinue()){
				payWithdrawals.setCallbackStatus(WithdrawalsStatusEnum.FAIL.getStatusCode());
			}else{
				payWithdrawals.setCallbackStatus(WithdrawalsStatusEnum.INVALID.getStatusCode());
			}
		}
		//C端展示提现失败原因
		if(payTransfersToWxChangeVO.getErrorCode()!=null){
			if(payTransfersToWxChangeVO.getErrorCode().equals(TransfersToWxError.MONEY_LIMIT.getCode())){
				payWithdrawals.setErrorMessage("当日累积限额超过2万，请重新提现。");
			}else if(payTransfersToWxChangeVO.getErrorCode().equals(TransfersToWxError.V2_ACCOUNT_SIMPLE_BAN.getCode())){
				payWithdrawals.setErrorMessage("请至微信实名认证后再次提现");

			}else if(payTransfersToWxChangeVO.getErrorCode().equals(TransfersToWxError.PARAM_ERROR.getCode())){
				//TODO 提示文案信息
				payWithdrawals.setErrorMessage("");
			}
		}

		payWithdrawals.setCallbackReason(payTransfersToWxChangeVO.getErrorDesc());
		payWithdrawals.setTransactionId(payTransfersToWxChangeVO.getPaymentNo());
		payWithdrawals.setTimeEnd(payTransfersToWxChangeVO.getPaymentTime());
		int transfersResult = this.transfersPublic(payWithdrawals,log);
		//成功发送云信
		if(payTransfersToWxChangeVO.isTransfersResult()){
			// 发送云信
			Calendar cal = Calendar.getInstance();
			cal.setTime(payWithdrawals.getCreated());
			int month=cal.get(Calendar.MONTH)+1;
			int day=cal.get(Calendar.DATE);
			String notifyMsg = PayNotifyMsg.STORE_SUCCESS_WITHDRWAL.replace("mm",String.valueOf(month)).replace("dd",String.valueOf(day));
			PayUtil.sendMsg(messageServiceClient,notifyMsg,MsgCategoryEnum.WITHDRAW_SUCCESS.getTypeCode(),payWithdrawals.getStoreId());
			PayUtil.sendSmsMsg(messageServiceClient,payWithdrawals.getMobile(),notifyMsg);
		}
		//失败根据code发送云信
		if(payTransfersToWxChangeVO.getErrorCode()!=null){
			if(payTransfersToWxChangeVO.getErrorCode().equals(TransfersToWxError.MONEY_LIMIT.getCode())){
				// 发送云信
				Calendar cal = Calendar.getInstance();
				cal.setTime(payWithdrawals.getCreated());
				int month=cal.get(Calendar.MONTH)+1;
				int day=cal.get(Calendar.DATE);
				String notifyMsg = PayNotifyMsg.STORE_MONEY_FAIL_WITHDRWAL.replace("mm",String.valueOf(month)).replace("dd",String.valueOf(day));
				PayUtil.sendMsg(messageServiceClient,notifyMsg,MsgCategoryEnum.WITHDRAW_FAIL.getTypeCode(),payWithdrawals.getStoreId());
				PayUtil.sendSmsMsg(messageServiceClient,payWithdrawals.getMobile(),notifyMsg);
			}else if(payTransfersToWxChangeVO.getErrorCode().equals(TransfersToWxError.V2_ACCOUNT_SIMPLE_BAN.getCode())){
				// 发送云信
				Calendar cal = Calendar.getInstance();
				cal.setTime(payWithdrawals.getCreated());
				int month=cal.get(Calendar.MONTH)+1;
				int day=cal.get(Calendar.DATE);
				String notifyMsg = PayNotifyMsg.STORE_FAIL_WITHDRWAL.replace("mm",String.valueOf(month)).replace("dd",String.valueOf(day));
				PayUtil.sendMsg(messageServiceClient,notifyMsg,MsgCategoryEnum.WITHDRAW_FAIL.getTypeCode(),payWithdrawals.getStoreId());
				PayUtil.sendSmsMsg(messageServiceClient,payWithdrawals.getMobile(),notifyMsg);
			}else if(payTransfersToWxChangeVO.getErrorCode().equals(TransfersToWxError.PARAM_ERROR.getCode())){
				//TODO 发送云信
			}
		}

		return transfersResult;
	}

	public int transfersPublic(PayWithdrawals payWithdrawals,String log){


        //step 1  修改提现申请状态
        int payWithdrawalsResult  = payWithdrawalsMapper.updateByWithdrawalsNoSelective(payWithdrawals);

		//step 2出账明细表 pay_finance_account_detail
		if(WithdrawalsStatusEnum.SUCCESS.getStatusCode() == payWithdrawals.getCallbackStatus()){
			PayFinanceAccountDetail payFinanceAccountDetail = new PayFinanceAccountDetail();
			payFinanceAccountDetail.setOrderNo(payWithdrawals.getWithdrawalsNo());
			payFinanceAccountDetail.setOutType(PayOutTypeEnum.STORE_WITHDRAW.getStatusCode());
			payFinanceAccountDetail.setStatus(StatusEnums.EFFECTIVE.getCode());
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
			payStoreTransactionRecord.setStatus(StatusEnums.EFFECTIVE.getCode());
			payStoreTransactionRecord.setStoreId(payWithdrawals.getStoreId());
			payStoreTransactionRecord.setMoney(payWithdrawals.getTotalFee());
			payStoreTransactionRecord.setCmmsAmt(payWithdrawals.getCmmsAmt());
			payStoreTransactionRecord.setTransactionDate(payWithdrawals.getCreated());
			payStoreCashService.savePayStoreTransactionRecord(payStoreTransactionRecord);
		}

        //step4 门店资金变化
		UpdateStoreBankRollCondition updateStoreBankRollCondition = new UpdateStoreBankRollCondition();
		updateStoreBankRollCondition.setStoreId(payWithdrawals.getStoreId());
		updateStoreBankRollCondition.setWithdrawalsNo(payWithdrawals.getWithdrawalsNo());
		updateStoreBankRollCondition.setMoney(payWithdrawals.getTotalFee());
		if(WithdrawalsStatusEnum.SUCCESS.getStatusCode() == payWithdrawals.getCallbackStatus()){
			updateStoreBankRollCondition.setType(StoreBankRollOpearateEnums.WITHDRAWALS_SUCCESS.getCode());
			this.updateStoreBankroll(updateStoreBankRollCondition);
		}else if(WithdrawalsStatusEnum.INVALID.getStatusCode() == payWithdrawals.getCallbackStatus()){
			updateStoreBankRollCondition.setType(StoreBankRollOpearateEnums.WITHDRAWALS_FAIL.getCode());
			this.updateStoreBankroll(updateStoreBankRollCondition);
		}else if(WithdrawalsStatusEnum.BANK_FAIL.getStatusCode() == payWithdrawals.getCallbackStatus()){
			updateStoreBankRollCondition.setType(StoreBankRollOpearateEnums.BANK_FAIL.getCode());
			this.updateStoreBankroll(updateStoreBankRollCondition);
		}
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
            logger.error(log+"--transfersService.transfersToChange返回结果为空");
			throw new BusinessException(BusinessCode.CODE_610039);
        }

		PayWithdrawals payWithdrawals = new PayWithdrawals();
		if(payTransfersToWxBankVO.isTransfersResult()){
			//提现到银行卡状态为微信处理中
			payWithdrawals.setCallbackStatus(WithdrawalsStatusEnum.HANDLE.getStatusCode());
		}else{
			//是否需要用户重新申请提现流程
			if(payTransfersToWxBankVO.isAbleContinue()){
				payWithdrawals.setCallbackStatus(WithdrawalsStatusEnum.FAIL.getStatusCode());
			}else{
				payWithdrawals.setCallbackStatus(WithdrawalsStatusEnum.INVALID.getStatusCode());

				//退回提现用户资金
				List<PayWithdrawals> payWithdrawalsList = payWithdrawalsMapper.selectByWithdrawalsNo(payTransfersToWxBankVO.getPartnerTradeNo());

				if (CollectionUtils.isNotEmpty(payWithdrawalsList)) {
					UpdateStoreBankRollCondition condition = new UpdateStoreBankRollCondition();
					condition.setType(StoreBankRollOpearateEnums.WITHDRAWALS_FAIL.getCode());
					condition.setStoreId(payWithdrawalsList.get(0).getStoreId());
					condition.setWithdrawalsNo(payWithdrawalsList.get(0).getWithdrawalsNo());
					condition.setMoney(payWithdrawalsList.get(0).getTotalFee());
					this.updateStoreBankroll(condition);
				}
			}
		}
		//C端展示提现失败原因
		if(payTransfersToWxBankVO.getErrorCode()!=null&&payTransfersToWxBankVO.getErrorDesc()!=null) {
			if (payTransfersToWxBankVO.getErrorCode().equals(TransfersToWxError.PARAM_ERROR.getCode()) && payTransfersToWxBankVO.getErrorDesc().indexOf("银行卡") >= 0) {
				payWithdrawals.setErrorMessage("银行卡信息有误，请重新填写。");
			} else if (payTransfersToWxBankVO.getErrorCode().equals(TransfersToWxError.AMOUNT_LIMIT.getCode())) {
				payWithdrawals.setErrorMessage("当日累积限额超过5万，请重新提现。");

			}
		}


		payWithdrawals.setCallbackReason(payTransfersToWxBankVO.getErrorDesc());
		payWithdrawals.setWithdrawalsNo(payTransfersToWxBankVO.getPartnerTradeNo());
		payWithdrawalsMapper.updateByWithdrawalsNoSelective(payWithdrawals);

		//提现失败根据code发送云信
		if(payTransfersToWxBankVO.getErrorCode()!=null&&payTransfersToWxBankVO.getErrorDesc()!=null) {
			List<PayWithdrawals> payWithdrawalsList = payWithdrawalsMapper.selectByWithdrawalsNo(payTransfersToWxBankVO.getPartnerTradeNo());
			if (payTransfersToWxBankVO.getErrorCode().equals(TransfersToWxError.PARAM_ERROR.getCode()) && payTransfersToWxBankVO.getErrorDesc().indexOf("银行卡") >= 0) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(payWithdrawalsList.get(0).getCreated());
				int month=cal.get(Calendar.MONTH)+1;
				int day=cal.get(Calendar.DATE);
				String notifyMsg = PayNotifyMsg.STORE_BANK_FAIL_WITHDRWAL.replace("mm",String.valueOf(month)).replace("dd",String.valueOf(day));
				PayUtil.sendMsg(messageServiceClient,notifyMsg,MsgCategoryEnum.WITHDRAW_FAIL.getTypeCode(),payWithdrawalsList.get(0).getStoreId());
				PayUtil.sendSmsMsg(messageServiceClient,payWithdrawalsList.get(0).getMobile(),notifyMsg);
			} else if (payTransfersToWxBankVO.getErrorCode().equals(TransfersToWxError.AMOUNT_LIMIT.getCode())) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(payWithdrawalsList.get(0).getCreated());
				int month=cal.get(Calendar.MONTH)+1;
				int day=cal.get(Calendar.DATE);
				String notifyMsg = PayNotifyMsg.STORE_MONEY_BANK_FAIL_WITHDRWAL.replace("mm",String.valueOf(month)).replace("dd",String.valueOf(day));
				PayUtil.sendMsg(messageServiceClient,notifyMsg,MsgCategoryEnum.WITHDRAW_FAIL.getTypeCode(),payWithdrawalsList.get(0).getStoreId());
				PayUtil.sendSmsMsg(messageServiceClient,payWithdrawalsList.get(0).getMobile(),notifyMsg);
			}
		}

		return 1;
	}

	@Override
	public int transfersPatrent(PayWithdrawalsCondition condition) {
		String  log =logLabel + "微信提现公共接口transfersPatrent";
		logger.info(log+"--开始");
		if (condition==null) {
			logger.error(log+"--参数为空");
			throw new BusinessException(BusinessCode.CODE_600101);
		}
		if (condition.getWithdrawalsId()==null) {
			logger.error(log+"--提现申请id为空");
			throw new BusinessException(BusinessCode.CODE_600310);
		}
		PayWithdrawals payWithdrawals = payWithdrawalsMapper.selectByPrimaryKey(condition.getWithdrawalsId());
		if(payWithdrawals == null){
			logger.error(log+"--提现申请不存在");
			throw new BusinessException(BusinessCode.CODE_600310);
		}
		//无效需要用户重新发起申请
		if(payWithdrawals.getCallbackStatus()!=null && payWithdrawals.getCallbackStatus() == WithdrawalsStatusEnum.INVALID.getStatusCode()){
			logger.error(log+"流水号{}审核失败,请重新发起申请",payWithdrawals.getWithdrawalsNo());
			throw new BusinessException(BusinessCode.CODE_600311);
		}

		if(payWithdrawals.getFlowDirectionType()== PayWithdrawalTypeEnum.BANKCARD_WITHDRAW.getStatusCode()){
			PayTransfersToWxBankCondition payTransfersToWxBankCondition = new PayTransfersToWxBankCondition();
			payTransfersToWxBankCondition.setPartnerTradeNo(payWithdrawals.getWithdrawalsNo());
			payTransfersToWxBankCondition.setAccount(payWithdrawals.getPaymentAccount());
			payTransfersToWxBankCondition.setTotalAmount(payWithdrawals.getRealFee());
			payTransfersToWxBankCondition.setAccountName(payWithdrawals.getName());
			payTransfersToWxBankCondition.setDesc(payWithdrawals.getName()+"用户提现,用户手机号:"+payWithdrawals.getMobile());
			payTransfersToWxBankCondition.setOperaterID(condition.getOperaterID());
			for(TransfersChannelCodeTypeEnum channelCodeTypeEnum : TransfersChannelCodeTypeEnum.values()){
				if(payWithdrawals.getSwiftCode().equals(String.valueOf(channelCodeTypeEnum.getCode()))){
					payTransfersToWxBankCondition.setChannelCode(channelCodeTypeEnum);
					break;
				}
			}
			return this.transfersToBank(payTransfersToWxBankCondition);
		}else{
			PayTransfersToWxChangeCondition toWxBalanceCondition = new PayTransfersToWxChangeCondition();
			toWxBalanceCondition.setPartnerTradeNo(payWithdrawals.getWithdrawalsNo());
			toWxBalanceCondition.setOperaterID(condition.getOperaterID());
			toWxBalanceCondition.setAccountId(payWithdrawals.getPaymentAccount());
			toWxBalanceCondition.setDesc(payWithdrawals.getName()+"用户提现,用户手机号:"+payWithdrawals.getMobile());
			toWxBalanceCondition.setTotalAmount(payWithdrawals.getRealFee());
			toWxBalanceCondition.setAccountName(payWithdrawals.getName());
			if(null == payWithdrawals.getSpbillCreateIp()){
				toWxBalanceCondition.setSpbillCreateIp("127.0.0.1");
			}else{
				toWxBalanceCondition.setSpbillCreateIp(payWithdrawals.getSpbillCreateIp());
			}
			return this.transfersToChange(toWxBalanceCondition);
		}
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

	@Override
	public Integer confirmTransferToBankStatus()  throws Exception {
		String  log =logLabel + "确认转账到银行卡记录状态confirmTransferToBankStatus";
		//待遍历集合
		List<PayWithdrawals> unclearStatus = getTransferToBankUnclearStatusWithdrawals();
		if(CollectionUtils.isEmpty(unclearStatus)){
			logger.info(log+"--不存在转至银行卡状态为处理中的提现记录");
			return 0;
		}
		int len = unclearStatus.size();
		//确认结果,更新提现状态
		for (int i = 0; i < len; i++){
			PayWithdrawals payWithdrawals = unclearStatus.get(i);
			PayTransfersQueryToWxBankVO resultForWxBank = wxTransfersService.getExactResultForWxBank(payWithdrawals.getWithdrawalsNo());
			String transfersStatus = resultForWxBank.getStatus();
			logger.info(log+"--提现流水号{},提现状态{}",payWithdrawals.getWithdrawalsNo(),transfersStatus);
			payWithdrawals.setCallbackReason(resultForWxBank.getReason());
			payWithdrawals.setCallbackCmmsAmt(resultForWxBank.getCmmsAmt());
			payWithdrawals.setTransactionId(resultForWxBank.getPaymentNo());
			//是否修改
			boolean doesModify = false;
			if (PayTransfersStatus.SUCCESS.getCode().equals(transfersStatus)) {
				payWithdrawals.setCallbackStatus(WithdrawalsStatusEnum.SUCCESS.getStatusCode());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				payWithdrawals.setTimeEnd(sdf.parse(resultForWxBank.getPaySuccTime()));
				doesModify = true;
			} else if (PayTransfersStatus.FAILED.getCode().equals(transfersStatus)) {
				payWithdrawals.setCallbackStatus(WithdrawalsStatusEnum.INVALID.getStatusCode());
				if(resultForWxBank.getPaySuccTime()!=null){
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					payWithdrawals.setTimeEnd(sdf.parse(resultForWxBank.getPaySuccTime()));
				}
				doesModify = true;
			}else if (PayTransfersStatus.BANK_FAIL.getCode().equals(transfersStatus)) {
				payWithdrawals.setErrorMessage(resultForWxBank.getReason());
				payWithdrawals.setCallbackStatus(WithdrawalsStatusEnum.BANK_FAIL.getStatusCode());
				if(resultForWxBank.getPaySuccTime()!=null){
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					payWithdrawals.setTimeEnd(sdf.parse(resultForWxBank.getPaySuccTime()));
				}
				doesModify = true;
			}

			//C端展示提现失败原因
			if(resultForWxBank.getErrCode()!=null&&resultForWxBank.getErrCodeDes()!=null){
				if(resultForWxBank.getErrCode().equals(TransfersToWxError.PARAM_ERROR.getCode())&&resultForWxBank.getErrCodeDes().indexOf("银行卡")>=0){
					payWithdrawals.setErrorMessage("银行卡信息有误，请重新填写。");
				}else if(resultForWxBank.getErrCode().equals(TransfersToWxError.AMOUNT_LIMIT.getCode())){
					payWithdrawals.setErrorMessage("当日累积限额超过5万，请重新提现。");
				}
			}
			if(doesModify){
				this.transfersPublic(payWithdrawals,log);
				wxTransfersService.modifyTransfersToBankStatus(resultForWxBank);
			}
			//提现成功发送云信
			if (PayTransfersStatus.SUCCESS.getCode().equals(transfersStatus)) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(payWithdrawals.getCreated());
				int month=cal.get(Calendar.MONTH)+1;
				int day=cal.get(Calendar.DATE);
				String notifyMsg = PayNotifyMsg.STORE_BANK_SUCCESS_WITHDRWAL.replace("mm",String.valueOf(month)).replace("dd",String.valueOf(day));
				PayUtil.sendMsg(messageServiceClient,notifyMsg,MsgCategoryEnum.WITHDRAW_SUCCESS.getTypeCode(),payWithdrawals.getStoreId());
				PayUtil.sendSmsMsg(messageServiceClient,payWithdrawals.getMobile(),notifyMsg);
			}
			//提现失败根据code发送云信
			if(resultForWxBank.getErrCode()!=null&&resultForWxBank.getErrCodeDes()!=null){
				if(resultForWxBank.getErrCode().equals(TransfersToWxError.PARAM_ERROR.getCode())&&resultForWxBank.getErrCodeDes().indexOf("银行卡")>=0){
					// 发送云信
					Calendar cal = Calendar.getInstance();
					cal.setTime(payWithdrawals.getCreated());
					int month=cal.get(Calendar.MONTH)+1;
					int day=cal.get(Calendar.DATE);
					String notifyMsg = PayNotifyMsg.STORE_BANK_FAIL_WITHDRWAL.replace("mm",String.valueOf(month)).replace("dd",String.valueOf(day));
					PayUtil.sendMsg(messageServiceClient,notifyMsg,MsgCategoryEnum.WITHDRAW_FAIL.getTypeCode(),payWithdrawals.getStoreId());
					PayUtil.sendSmsMsg(messageServiceClient,payWithdrawals.getMobile(),notifyMsg);
				}else if(resultForWxBank.getErrCode().equals(TransfersToWxError.AMOUNT_LIMIT.getCode())){
					Calendar cal = Calendar.getInstance();
					cal.setTime(payWithdrawals.getCreated());
					int month=cal.get(Calendar.MONTH)+1;
					int day=cal.get(Calendar.DATE);
					String notifyMsg = PayNotifyMsg.STORE_MONEY_BANK_FAIL_WITHDRWAL.replace("mm",String.valueOf(month)).replace("dd",String.valueOf(day));
					PayUtil.sendMsg(messageServiceClient,notifyMsg,MsgCategoryEnum.WITHDRAW_FAIL.getTypeCode(),payWithdrawals.getStoreId());
					PayUtil.sendSmsMsg(messageServiceClient,payWithdrawals.getMobile(),notifyMsg);
				}
			}
		}
		return 1;
	}

	/**
	 * 获得所有转至银行卡状态处理中的提现记录
	 * @return
	 */
	private List<PayWithdrawals> getTransferToBankUnclearStatusWithdrawals(){
		return payWithdrawalsMapper.selectTransferToBankUnclearStatusWithdrawals();
	}
    /**
     * @author liuhanning
     * @date  2018年8月23日 下午5:47:47
     * @Description 订单闭环，添加交易记录
     * @param orderNo
     * @param orderInfo
     */
	@Override
    public void orderFinishHandler(String orderNo, OrderInfo orderInfo) {
        //计算门店资金
        //手续费
        BigDecimal cmmsAmt = orderInfo.getRealPaymentMoney().multiply(WXCalculation.FEE_RATE_OF_WX).setScale(WXCalculation.DECIMAL_NUMBER, WXCalculation.DECIMAL_CALCULATION);
        //门店应得金额（订单总额-手续费）
        BigDecimal money = orderInfo.getOrderTotalMoney().subtract(cmmsAmt);
        UpdateStoreBankRollCondition condition = new UpdateStoreBankRollCondition();
        condition.setOrderNo(orderNo);
        condition.setStoreId(orderInfo.getStoreId());
        condition.setMoney(money);
        condition.setType(StoreBankRollOpearateEnums.ORDER_FINISH.getCode());
        updateStoreBankroll(condition);
        logger.info("订单闭环，添加交易记录 condition:{}", condition.toString());
        //添加交易记录
        PayStoreTransactionRecord payStoreTransactionRecord = new PayStoreTransactionRecord();
        payStoreTransactionRecord.setStoreId(orderInfo.getStoreId());
        payStoreTransactionRecord.setOrderNo(orderNo);
        payStoreTransactionRecord.setType(StoreTransactionStatusEnum.ORDER_ENTRY.getStatusCode());
        payStoreTransactionRecord.setMoney(money);
        payStoreTransactionRecord.setRate(WXCalculation.FEE_RATE_OF_WX);
        payStoreTransactionRecord.setCmmsAmt(cmmsAmt);
        logger.info("订单闭环，添加交易记录 record:{}", payStoreTransactionRecord.toString());
        payStoreCashService.savePayStoreTransactionRecord(payStoreTransactionRecord);
    }
}
