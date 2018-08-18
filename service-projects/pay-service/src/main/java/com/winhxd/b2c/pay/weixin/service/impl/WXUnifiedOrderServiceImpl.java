package com.winhxd.b2c.pay.weixin.service.impl;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.winhxd.b2c.common.domain.pay.condition.PayPreOrderCondition;
import com.winhxd.b2c.common.domain.pay.vo.PayPreOrderVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.pay.weixin.base.dto.PayPreOrderDTO;
import com.winhxd.b2c.pay.weixin.base.dto.PayPreOrderResponseDTO;
import com.winhxd.b2c.pay.weixin.base.wxpayapi.WXPayApi;
import com.winhxd.b2c.pay.weixin.constant.BillStatusEnum;
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
	//小程序预支付标识
	private static final String PACKAGE = "prepay_id=";
	private static final int TRADE_NO_MAX_LENGTH = 32;
	
	@Autowired
	private PayBillMapper payBillMapper;
	@Autowired
	WXPayApi wxPayApi;

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
		PayPreOrderDTO payPreOrderDTO = new PayPreOrderDTO();
		BeanUtils.copyProperties(condition, payPreOrderDTO);
		// 生产支付流水号
		long timeStamp = System.currentTimeMillis();
		String outTradeNo = generateOutTradeNo(condition.getOutOrderNo(), timeStamp);
		
		payPreOrderDTO.setOutTradeNo(outTradeNo);
		
        //调用微信统一下单API
		PayPreOrderResponseDTO payPreOrderResponseDTO = wxPayApi.unifiedOrder(payPreOrderDTO);
		// 保存支付流水记录
		this.savePayBill(payPreOrderDTO, payPreOrderResponseDTO);
		
		//初始化反参
		payPreOrderVO.setAppId(payPreOrderDTO.getAppid());
		payPreOrderVO.setNonceStr(payPreOrderDTO.getNonceStr());
		payPreOrderVO.setOutOrderNo(condition.getOutOrderNo());
		payPreOrderVO.setOutTradeNo(outTradeNo);
		payPreOrderVO.setPackageData(PACKAGE + payPreOrderResponseDTO.getPrepayId());
		payPreOrderVO.setSignType(payPreOrderDTO.getSignType());
		payPreOrderVO.setTimeStamp(String.valueOf(timeStamp));
		
		return payPreOrderVO;
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
	 * 支付完成
	 * @author mahongliang
	 * @date  2018年8月17日 下午5:35:58
	 * @Description 
	 * @param condition
	 * @return
	 */
	private void paid(PayPreOrderCondition condition) {
		logger.warn("订单{}支付中，请勿重复支付", condition.getOutOrderNo());
		throw new BusinessException(340001, "支付中，请勿重复支付");
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
	
	private int savePayBill(PayPreOrderDTO payPreOrderDTO, PayPreOrderResponseDTO payPreOrderResponseDTO) {
		PayBill bill = new PayBill();
		
		return payBillMapper.insertSelective(bill);
	}

}
