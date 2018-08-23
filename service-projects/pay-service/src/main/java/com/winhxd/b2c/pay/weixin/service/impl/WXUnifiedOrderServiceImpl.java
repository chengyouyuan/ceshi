package com.winhxd.b2c.pay.weixin.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.CurrencyEnum;
import com.winhxd.b2c.common.constant.TradeTypeEnum;
import com.winhxd.b2c.common.domain.pay.condition.PayPreOrderCondition;
import com.winhxd.b2c.common.domain.pay.vo.PayPreOrderVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.pay.weixin.base.dto.PayOrderQueryDTO;
import com.winhxd.b2c.pay.weixin.base.dto.PayPreOrderCallbackDTO;
import com.winhxd.b2c.pay.weixin.base.dto.PayPreOrderDTO;
import com.winhxd.b2c.pay.weixin.base.dto.PayPreOrderResponseDTO;
import com.winhxd.b2c.pay.weixin.base.wxpayapi.WXPayApi;
import com.winhxd.b2c.pay.weixin.constant.BillStatusEnum;
import com.winhxd.b2c.pay.weixin.constant.TradeStateEnum;
import com.winhxd.b2c.pay.weixin.dao.PayBillMapper;
import com.winhxd.b2c.pay.weixin.model.PayBill;
import com.winhxd.b2c.pay.weixin.service.WXUnifiedOrderService;

/**
 * 支付网关微信统一下单API实现
 * @author mahongliang
 * @date  2018年8月15日 下午2:10:57
 * @Description 
 * @version
 */
@Service
public class WXUnifiedOrderServiceImpl implements WXUnifiedOrderService {
	private static final Logger logger = LoggerFactory.getLogger(WXUnifiedOrderServiceImpl.class);
	/**
	 * 小程序预支付标识
	 */
	private static final String PACKAGE = "prepay_id=";
	/**
	 * 支付流水号最大长度
	 */
	private static final int TRADE_NO_MAX_LENGTH = 32;
	//支付凭证过期时间，微信方2小时，此处默认一小时后再次生产
	private static final int PAY_TIME_EXPIRE = 1 * 3600 * 1000;
	
	@Autowired
	private PayBillMapper payBillMapper;
	@Autowired
	private WXPayApi wxPayApi;

	@Override
	public PayPreOrderVO unifiedOrder(PayPreOrderCondition condition) {
		PayPreOrderVO payPreOrderVO = null;
		//真实订单号
		String outOrderNo = condition.getOutOrderNo();
		//支付状态：0.支付中，1.支付完成，2.支付失败
		List<PayBill> list =  payBillMapper.selectByOutOrderNo(outOrderNo);
		PayBill bill = null;
		//去支付
		if(CollectionUtils.isEmpty(list)) {
			payPreOrderVO = toPay(condition);
			
		//支付完成	
		} else if((bill = this.getPayBill(list, BillStatusEnum.PAID.getCode())) != null) {
			paid(condition);
			
		//支付中
		} else if((bill = this.getPayBill(list, BillStatusEnum.PAYING.getCode())) != null) {
			payPreOrderVO = paying(condition, bill);
		}
		
		return payPreOrderVO;
	}
	
	@Override
	public PayBill updatePayBillByOutTradeNo(PayPreOrderCallbackDTO payPreOrderCallbackDTO, Short status) {
		String outTradeNo = payPreOrderCallbackDTO.getOutTradeNo();
		PayBill bill = payBillMapper.selectByOutTradeNo(outTradeNo);
		bill.setStatus(status);
		bill.setIsSubscribe(payPreOrderCallbackDTO.getIsSubscribe());
		bill.setBankType(payPreOrderCallbackDTO.getBankType());
		bill.setSettlementTotalFee(payPreOrderCallbackDTO.getSettlementTotalFee());
		if(payPreOrderCallbackDTO.getSettlementTotalFee() != null) {
			bill.setSettlementTotalAmount(new BigDecimal(payPreOrderCallbackDTO.getSettlementTotalFee()).multiply(new BigDecimal(0.01)));
		}
		bill.setFeeType(payPreOrderCallbackDTO.getFeeType());
		bill.setCashFee(payPreOrderCallbackDTO.getCashFee());
		bill.setCashFeeType(payPreOrderCallbackDTO.getCashFeeType());
		bill.setCouponFee(payPreOrderCallbackDTO.getCashFee());
		bill.setCouponCount(payPreOrderCallbackDTO.getCouponCount());
		bill.setTimeEnd(payPreOrderCallbackDTO.getTimeEnd());
		bill.setCallbackTotalFee(payPreOrderCallbackDTO.getTotalFee());
		if(payPreOrderCallbackDTO.getTotalFee() != null) {
			bill.setCallbackTotalAmount(new BigDecimal(payPreOrderCallbackDTO.getTotalFee()).multiply(new BigDecimal(0.01)));
		}
		bill.setTradeState(payPreOrderCallbackDTO.getTradeState());
		bill.setTradeStateDesc(payPreOrderCallbackDTO.getTradeStateDesc());
		
		payBillMapper.updateByPrimaryKeySelective(bill);
		
		return bill;
	}
	
	/**
	 * 去支付
	 * @author mahongliang
	 * @date  2018年8月17日 下午5:35:58
	 * @Description 
	 * @param condition
	 * @return
	 * @throws Exception 
	 */
	private PayPreOrderVO toPay(PayPreOrderCondition condition) {
		PayPreOrderVO payPreOrderVO = new PayPreOrderVO();
		//微信接口入参
		PayPreOrderDTO payPreOrderDTO = this.generatePayPreOrderDTO(condition);
		// 生产支付流水号
		long timeStamp = System.currentTimeMillis();
		String outTradeNo = generateOutTradeNo(condition.getOutOrderNo(), timeStamp);
		payPreOrderDTO.setOutTradeNo(outTradeNo);
		payPreOrderDTO.setTradeType(TradeTypeEnum.WECHAT_H5.getCode());
		
        //调用微信统一下单API
		PayPreOrderResponseDTO payPreOrderResponseDTO = wxPayApi.unifiedOrder(payPreOrderDTO);
		if(payPreOrderResponseDTO == null) {
			logger.error("获取支付凭证响应为空，内部错误");
			throw new BusinessException(BusinessCode.CODE_1001);
		}
		
		if(PayPreOrderResponseDTO.FAIL.equals(payPreOrderResponseDTO.getReturnCode()) ||
				PayPreOrderResponseDTO.FAIL.equals(payPreOrderResponseDTO.getResultCode())) {
			logger.error(payPreOrderResponseDTO.getReturnMsg());
			throw new BusinessException(BusinessCode.CODE_3400910, payPreOrderResponseDTO.getReturnMsg());
		}
		// 保存支付流水记录
		this.savePayBill(condition, payPreOrderDTO, payPreOrderResponseDTO);
		
		//初始化反参
		payPreOrderVO.setAppId(payPreOrderDTO.getAppid());
		payPreOrderVO.setNonceStr(payPreOrderDTO.getNonceStr());
		payPreOrderVO.setPackageData(PACKAGE + payPreOrderResponseDTO.getPrepayId());
		payPreOrderVO.setSignType(payPreOrderDTO.getSignType());
		payPreOrderVO.setTimeStamp(String.valueOf(timeStamp));
		//只对前5个参数签名，对package做特殊处理
		payPreOrderVO.setPaySign(wxPayApi.payPreOrderSign(payPreOrderVO));
		payPreOrderVO.setOutOrderNo(condition.getOutOrderNo());
		payPreOrderVO.setOutTradeNo(outTradeNo);
		payPreOrderVO.setPayStatus(true);
		
		return payPreOrderVO;
	}
	
	/**
	 * 支付完成
	 * @author mahongliang
	 * @date  2018年8月17日 下午5:35:58
	 * @Description 
	 * @param condition
	 * @return
	 */
	private void paid(PayPreOrderCondition condition) {
		logger.warn("订单{}支付完成，请勿重复支付", condition.getOutOrderNo());
		throw new BusinessException(BusinessCode.CODE_3400908, "支付中，请勿重复支付");
	}
	
	/**
	 * 支付中
	 * @author mahongliang
	 * @date  2018年8月17日 下午5:35:58
	 * @Description 
	 * @param condition
	 * @param bill
	 * @return
	 */
	private PayPreOrderVO paying(PayPreOrderCondition condition, PayBill bill) {
		PayPreOrderVO payPreOrderVO = null;
		// 主动查询，更新流水
		PayOrderQueryDTO payOrderQueryDTO = new PayOrderQueryDTO();
		String outTradeNo = bill.getOutTradeNo();
		payOrderQueryDTO.setOutTradeNo(outTradeNo);
		PayPreOrderCallbackDTO payPreOrderCallbackDTO = wxPayApi.orderQuery(payOrderQueryDTO);
		//查询响应错误
		if(payPreOrderCallbackDTO == null || 
				PayPreOrderCallbackDTO.FAIL.equals(payPreOrderCallbackDTO.getReturnCode())) {
			logger.error("主动查询{}返回错误：{}", outTradeNo, payPreOrderCallbackDTO.getReturnMsg());
			throw new BusinessException(BusinessCode.CODE_3400910, payPreOrderCallbackDTO.getReturnMsg());
		}
		//查询支付失败
		if(PayPreOrderCallbackDTO.FAIL.equals(payPreOrderCallbackDTO.getResultCode())) {
			logger.warn("上次支付{}失败，再次发起支付", outTradeNo);
			//更新上次支付失败记录
			this.updatePayBillByOutTradeNo(payPreOrderCallbackDTO, BillStatusEnum.FAIL.getCode());
			//重新发起支付
			return this.toPay(condition);
		}
		
		//主动查询状态处理
		if(TradeStateEnum.SUCCESS.getCode().equals(payPreOrderCallbackDTO.getTradeState())) {
			logger.warn("主动查询{}交易：{}", outTradeNo, payPreOrderCallbackDTO.getErrCodeDes());
			this.updatePayBillByOutTradeNo(payPreOrderCallbackDTO, BillStatusEnum.PAID.getCode());
			throw new BusinessException(BusinessCode.CODE_3400908, "支付完成，请勿重复支付");
			
		} else if(TradeStateEnum.REFUND.getCode().equals(payPreOrderCallbackDTO.getTradeState())) {
			logger.warn("主动查询{}交易：{}", outTradeNo, payPreOrderCallbackDTO.getErrCodeDes());
			this.updatePayBillByOutTradeNo(payPreOrderCallbackDTO, BillStatusEnum.PAID.getCode());
			throw new BusinessException(BusinessCode.CODE_3400908, "支付完成，请勿重复支付");
			
		} else if(TradeStateEnum.NOTPAY.getCode().equals(payPreOrderCallbackDTO.getTradeState())) {
			logger.warn("主动查询{}交易：{}", outTradeNo, payPreOrderCallbackDTO.getErrCodeDes());
			//支付凭证是否过期
			long timeExpire = bill.getTimeExpire() == null ? 0 : bill.getTimeExpire().getTime();
			if(timeExpire == 0) {
				timeExpire = bill.getCreated().getTime() + PAY_TIME_EXPIRE;
			}
			//已过期
			if(System.currentTimeMillis() > timeExpire) {
				logger.warn("订单{}交易流水号{}已过期，重新生产", bill.getOutOrderNo(), outTradeNo);
				this.updatePayBillByOutTradeNo(payPreOrderCallbackDTO, BillStatusEnum.FAIL.getCode());
				//再次去支付
				payPreOrderVO = toPay(condition);
			} else {
				//未过期，使用原来流水
				payPreOrderVO = returnPayPreOrderVO(bill);
			}
			
		} else if(TradeStateEnum.CLOSED.getCode().equals(payPreOrderCallbackDTO.getTradeState())) {
			logger.warn("主动查询{}交易：{}", outTradeNo, payPreOrderCallbackDTO.getErrCodeDes());
			this.updatePayBillByOutTradeNo(payPreOrderCallbackDTO, BillStatusEnum.FAIL.getCode());
			//再次去支付
			payPreOrderVO = toPay(condition);
			
		} else if(TradeStateEnum.REVOKED.getCode().equals(payPreOrderCallbackDTO.getTradeState())) {
			logger.warn("主动查询{}交易：{}", outTradeNo, payPreOrderCallbackDTO.getErrCodeDes());
			this.updatePayBillByOutTradeNo(payPreOrderCallbackDTO, BillStatusEnum.FAIL.getCode());
			//再次去支付
			payPreOrderVO = toPay(condition);
			
		} else if(TradeStateEnum.USERPAYING.getCode().equals(payPreOrderCallbackDTO.getTradeState())) {
			logger.warn("主动查询{}交易：{}", outTradeNo, payPreOrderCallbackDTO.getErrCodeDes());
			throw new BusinessException(BusinessCode.CODE_3400900, "支付中，请勿重复支付");
			
			//TODO 处理 USERPAYING--用户支付中，支付时间大于10分钟，调起关闭订单，成功后再次支付
		} else if(TradeStateEnum.PAYERROR.getCode().equals(payPreOrderCallbackDTO.getTradeState())) {
			logger.warn("主动查询{}交易：{}", outTradeNo, payPreOrderCallbackDTO.getErrCodeDes());
			this.updatePayBillByOutTradeNo(payPreOrderCallbackDTO, BillStatusEnum.FAIL.getCode());
			//再次去支付
			payPreOrderVO = toPay(condition);
			
		} else {
			logger.error("主动查询{}交易状态为{}：{}", outTradeNo, payPreOrderCallbackDTO.getTradeState(), payPreOrderCallbackDTO.getErrCodeDes());
			throw new BusinessException(BusinessCode.CODE_1001);
		}
		
		return payPreOrderVO;
	}
	
	/**
	 * 返回支付凭证
	 * @author mahongliang
	 * @date  2018年8月21日 下午5:35:57
	 * @Description 
	 * @param bill
	 * @return
	 */
	private PayPreOrderVO returnPayPreOrderVO(PayBill bill) {
		PayPreOrderVO payPreOrderVO = new PayPreOrderVO();
		
		//初始化反参
		payPreOrderVO.setAppId(bill.getAppid());
		payPreOrderVO.setNonceStr(bill.getNonceStr());
		payPreOrderVO.setPackageData(PACKAGE + bill.getPrepayId());
		payPreOrderVO.setSignType(bill.getSignType());
		//TODO 此处随机数和上一次不同，是否正确需要试一下
		payPreOrderVO.setTimeStamp(String.valueOf(System.currentTimeMillis()));
		//只对前5个参数签名，对package做特殊处理
		payPreOrderVO.setPaySign(wxPayApi.payPreOrderSign(payPreOrderVO));
		payPreOrderVO.setPayStatus(false);
		payPreOrderVO.setOutOrderNo(bill.getOutOrderNo());
		payPreOrderVO.setOutTradeNo(bill.getOutTradeNo());
		
		return payPreOrderVO;
	}
	
	/**
	 * 根据状态查询订单流水
	 * @author mahongliang
	 * @date  2018年8月19日 下午5:22:22
	 * @Description 
	 * @param list
	 * @param status
	 * @return
	 */
	private PayBill getPayBill(List<PayBill> list, Short status){
		PayBill bill = null;
		for(PayBill pb : list) {
			if(status.equals(pb.getStatus())){
				bill = pb;
				break;
			}
		}
		return bill;
	}
	
	/**
	 * 调用微信api入参
	 * @author mahongliang
	 * @date  2018年8月18日 下午6:43:53
	 * @Description 
	 * @param condition
	 * @return
	 */
	private PayPreOrderDTO generatePayPreOrderDTO(PayPreOrderCondition condition) {
		//微信接口入参
		PayPreOrderDTO payPreOrderDTO = new PayPreOrderDTO();
		BeanUtils.copyProperties(condition, payPreOrderDTO);
		//支付金额，单位为分
		payPreOrderDTO.setTotalFee(condition.getTotalAmount().multiply(new BigDecimal(100)).intValue());
		payPreOrderDTO.setTimeStart(new Date());
		payPreOrderDTO.setFeeType(CurrencyEnum.CNY.getCode());
		
		return payPreOrderDTO;
	}
	
	/**
	 * 生产交易流水号
	 * @author mahongliang
	 * @date  2018年8月18日 下午5:53:25
	 * @Description 
	 * @param outOrderNo	真实订单号
	 * @param timeStamp		时间差
	 * @return
	 */
	private String generateOutTradeNo(String outOrderNo, long timeStamp) {
		String outTradeNo = outOrderNo + '_' + timeStamp;
		int outTradeNoLength = outTradeNo.length();
		if(outTradeNoLength > TRADE_NO_MAX_LENGTH) {
			int beginIndex = outTradeNoLength - TRADE_NO_MAX_LENGTH;
			//截取前几位，保留32个字符
			outTradeNo = outTradeNo.substring(beginIndex, outTradeNoLength);
		}
		return outTradeNo;
	}
	
	/**
	 * 
	 * @author mahongliang
	 * @date  2018年8月18日 下午6:27:29
	 * @Description 
	 * @param PayPreOrderCondition		外部调用入参
	 * @param payPreOrderDTO			调用微信api入参
	 * @param payPreOrderResponseDTO	调用微信api反参
	 * @return
	 */
	private int savePayBill(PayPreOrderCondition condition, PayPreOrderDTO payPreOrderDTO, PayPreOrderResponseDTO payPreOrderResponseDTO) {
		PayBill bill = new PayBill();
		bill.setOutOrderNo(condition.getOutOrderNo());
		bill.setTotalAmount(condition.getTotalAmount());
		
		bill.setAppid(payPreOrderDTO.getAppid());
		bill.setAttach(payPreOrderDTO.getAttach());
		bill.setBody(payPreOrderDTO.getBody());
		bill.setBuyerId(payPreOrderDTO.getOpenid());
		bill.setCreated(new Date());
		bill.setDeviceInfo(payPreOrderDTO.getDeviceInfo());
		bill.setFeeType(payPreOrderDTO.getFeeType());
		bill.setLimitPay(payPreOrderDTO.getLimitPay());
		bill.setMchId(payPreOrderDTO.getMchId());
		bill.setNonceStr(payPreOrderDTO.getNonceStr());
		bill.setNotifyUrl(payPreOrderDTO.getNotifyUrl());
		bill.setOutTradeNo(payPreOrderDTO.getOutTradeNo());
		bill.setProductId(payPreOrderDTO.getProductId());
		bill.setSpbillCreateIp(payPreOrderDTO.getSpbillCreateIp());
		bill.setTimeStart(payPreOrderDTO.getTimeStart());
		bill.setTotalFee(payPreOrderDTO.getTotalFee());
		bill.setTradeType(payPreOrderDTO.getTradeType());
		bill.setSignType(payPreOrderDTO.getSignType());
		bill.setSign(payPreOrderDTO.getSign());
		if(PayPreOrderResponseDTO.FAIL.equals(payPreOrderResponseDTO.getResultCode())) {
			bill.setErrorCode(payPreOrderResponseDTO.getErrCode());
			bill.setErrorMessage(payPreOrderResponseDTO.getErrCodeDes());
			bill.setStatus((short) 2);
		} else {
			bill.setStatus((short) 0);
			bill.setPrepayId(payPreOrderResponseDTO.getPrepayId());
			bill.setCodeUrl(payPreOrderResponseDTO.getCode_url());
		}
		
		return payBillMapper.insertSelective(bill);
	}

}
