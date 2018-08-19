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

import com.winhxd.b2c.common.constant.Currency;
import com.winhxd.b2c.common.constant.TradeType;
import com.winhxd.b2c.common.domain.pay.condition.PayPreOrderCondition;
import com.winhxd.b2c.common.domain.pay.vo.PayPreOrderVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.pay.weixin.base.dto.PayPreOrderCallbackDTO;
import com.winhxd.b2c.pay.weixin.base.dto.PayPreOrderDTO;
import com.winhxd.b2c.pay.weixin.base.dto.PayPreOrderResponseDTO;
import com.winhxd.b2c.pay.weixin.base.wxpayapi.WXPayApi;
import com.winhxd.b2c.pay.weixin.constant.BillStatusEnum;
import com.winhxd.b2c.pay.weixin.dao.PayBillMapper;
import com.winhxd.b2c.pay.weixin.model.PayBill;
import com.winhxd.b2c.pay.weixin.service.WXUnifiedOrderService;
import com.winhxd.b2c.pay.weixin.util.DateUtil;

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
	//小程序预支付标识
	private static final String PACKAGE = "prepay_id=";
	//支付流水号最大长度
	private static final int TRADE_NO_MAX_LENGTH = 32;
	//微信api时间格式
	private static final String DATE_FORMAT = "yyyyMMddHHmmss";
	
	@Autowired
	private PayBillMapper payBillMapper;
	@Autowired
	private WXPayApi wxPayApi;

	@Override
	public PayPreOrderVO unifiedOrder(PayPreOrderCondition condition) {
		PayPreOrderVO payPreOrderVO = null;
		//真实订单号
		String outOrderNo = condition.getOutOrderNo();
		List<Integer> statusList =  payBillMapper.selectPayBillStatusByOutOrderNo(outOrderNo);
		
		//去支付
		if(CollectionUtils.isEmpty(statusList)) {
			payPreOrderVO = toPay(condition);
			
		//支付完成	
		} else if(statusList.contains(BillStatusEnum.PAID.getCode())) {
			paid(condition);
			
		//支付中
		} else if(statusList.contains(BillStatusEnum.PAYING.getCode())) {
			payPreOrderVO = paying(condition);
		}
		
		return payPreOrderVO;
	}
	
	@Override
	public PayBill updatePayBillByOutTradeNo(PayPreOrderCallbackDTO payPreOrderCallbackDTO) {
		String outTradeNo = payPreOrderCallbackDTO.getOutTradeNo();
		PayBill bill = payBillMapper.selectByOutTradeNo(outTradeNo);
		bill.setIsSubscribe(payPreOrderCallbackDTO.getIsSubscribe());
		bill.setBankType(payPreOrderCallbackDTO.getBankType());
		bill.setSettlementTotalFee(payPreOrderCallbackDTO.getSettlementTotalFee());
		bill.setSettlementTotalAmount(new BigDecimal(payPreOrderCallbackDTO.getSettlementTotalFee()).multiply(new BigDecimal(0.01)));
		bill.setFeeType(payPreOrderCallbackDTO.getFeeType());
		bill.setCashFee(payPreOrderCallbackDTO.getCashFee());
		bill.setCashFeeType(payPreOrderCallbackDTO.getCashFeeType());
		bill.setCouponFee(payPreOrderCallbackDTO.getCashFee());
		bill.setCouponCount(payPreOrderCallbackDTO.getCouponCount());
		bill.setTimeEnd(payPreOrderCallbackDTO.getTimeEnd());
		bill.setCallbackTotalFee(payPreOrderCallbackDTO.getTotalFee());
		bill.setCallbackTotalAmount(new BigDecimal(payPreOrderCallbackDTO.getTotalFee()).multiply(new BigDecimal(0.01)));
		
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
		payPreOrderDTO.setTradeType(TradeType.WECHAT_H5.getCode());
		
        //调用微信统一下单API
		PayPreOrderResponseDTO payPreOrderResponseDTO = wxPayApi.unifiedOrder(payPreOrderDTO);
		if(payPreOrderResponseDTO == null ||PayPreOrderResponseDTO.FAIL.equals(payPreOrderResponseDTO.getReturnCode())) {
			logger.error(payPreOrderResponseDTO.getReturnMsg());
			throw new BusinessException(3400910, payPreOrderResponseDTO.getReturnMsg());
		}
		// 保存支付流水记录
		this.savePayBill(condition, payPreOrderDTO, payPreOrderResponseDTO);
		
		//初始化反参
		payPreOrderVO.setAppId(payPreOrderDTO.getAppid());
		payPreOrderVO.setNonceStr(payPreOrderDTO.getNonceStr());
		payPreOrderVO.setOutOrderNo(condition.getOutOrderNo());
		payPreOrderVO.setOutTradeNo(outTradeNo);
		payPreOrderVO.setPackageData(PACKAGE + payPreOrderResponseDTO.getPrepayId());
		payPreOrderVO.setSignType(payPreOrderDTO.getSignType());
		payPreOrderVO.setTimeStamp(String.valueOf(timeStamp));
		payPreOrderVO.setPaySign(wxPayApi.generateSign(payPreOrderVO));
		
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
		logger.warn("订单{}支付中，请勿重复支付", condition.getOutOrderNo());
		throw new BusinessException(3400900, "支付中，请勿重复支付");
	}
	
	/**
	 * 去支付
	 * @author mahongliang
	 * @date  2018年8月17日 下午5:35:58
	 * @Description 
	 * @param condition
	 * @return
	 */
	private PayPreOrderVO paying(PayPreOrderCondition condition) {
		PayPreOrderVO payPreOrderVO = new PayPreOrderVO();
		
		return payPreOrderVO;
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
		payPreOrderDTO.setTimeStart(DateUtil.format(new Date(), DATE_FORMAT));
		payPreOrderDTO.setFeeType(Currency.CNY.getText());
		
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
		bill.setNotifyUrl(payPreOrderDTO.getNonceStr());
		bill.setOutTradeNo(payPreOrderDTO.getOutTradeNo());
		bill.setProductId(payPreOrderDTO.getProductId());
		bill.setSpbillCreateIp(payPreOrderDTO.getSpbillCreateIp());
		bill.setTimeStart(DateUtil.toDate(payPreOrderDTO.getTimeStart(), DATE_FORMAT));
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
