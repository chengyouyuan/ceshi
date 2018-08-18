package com.winhxd.b2c.pay.weixin.service.impl;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.winhxd.b2c.common.domain.pay.condition.PayPreOrderCondition;
import com.winhxd.b2c.common.domain.pay.vo.OrderPayVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.pay.weixin.base.dto.PayPreOrderDTO;
import com.winhxd.b2c.pay.weixin.base.dto.PayPreOrderResponseDTO;
import com.winhxd.b2c.pay.weixin.base.wxpayapi.WXPayApi;
import com.winhxd.b2c.pay.weixin.constant.BillStatusEnum;
import com.winhxd.b2c.pay.weixin.dao.PayBillMapper;
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
	
	@Autowired
	private PayBillMapper payBillMapper;
	@Autowired
	WXPayApi wxPayApi;

	@Override
	public OrderPayVO unifiedOrder(PayPreOrderCondition condition) {
		OrderPayVO orderPayVO = null;
		//真实订单号
		String outOrderNo = condition.getOutOrderNo();
		List<Integer> statusList =  payBillMapper.selectPayBillStatusByOutOrderNo(outOrderNo);
		
		//去支付
		if(CollectionUtils.isEmpty(statusList)) {
			orderPayVO = toPay(condition);
			
		//支付完成	
		} else if(statusList.contains(BillStatusEnum.PAID.getCode())) {
			paid(condition);
			
		//支付中
		} else if(statusList.contains(BillStatusEnum.PAYING.getCode())) {
			orderPayVO = paying(condition);
		}
		
		return orderPayVO;
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
	private OrderPayVO toPay(PayPreOrderCondition condition) {
		OrderPayVO orderPayVO = new OrderPayVO();
		//微信接口入参
		PayPreOrderDTO payPreOrderDTO = new PayPreOrderDTO();
		BeanUtils.copyProperties(condition, payPreOrderDTO);
		// TODO 生产支付流水号
		payPreOrderDTO.setOutTradeNo("dsafdsakfjasdkjf");
		
        //调用微信统一下单API
		PayPreOrderResponseDTO payPreOrderResponseDTO = wxPayApi.unifiedOrder(payPreOrderDTO);
		// TODO保存支付流水记录
		
		return orderPayVO;
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
	private OrderPayVO paying(PayPreOrderCondition condition) {
		OrderPayVO orderPayVO = new OrderPayVO();
		
		return orderPayVO;
	}

}
