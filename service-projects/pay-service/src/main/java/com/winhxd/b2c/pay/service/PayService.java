package com.winhxd.b2c.pay.service;

import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.pay.condition.OrderPayCallbackCondition;
import com.winhxd.b2c.common.domain.pay.condition.OrderPayCondition;
import com.winhxd.b2c.common.domain.pay.condition.OrderRefundCondition;
import com.winhxd.b2c.common.domain.pay.condition.StoreBankrollChangeCondition;
import com.winhxd.b2c.common.domain.pay.condition.UpdateOrderCondition;
import com.winhxd.b2c.common.domain.pay.model.StoreBankroll;
import com.winhxd.b2c.common.domain.pay.vo.OrderPayVO;
import com.winhxd.b2c.common.domain.pay.vo.OrderRefundVO;

/**
 * @author liuhanning
 * @date  2018年8月11日 下午4:09:33
 * @Description 支付service
 * @version 
 */
public interface PayService {

	
	/**
	 * @author liuhanning
	 * @date  2018年8月13日 下午12:46:25
	 * @Description 退款
	 * @return
	 */
	ResponseResult<OrderRefundVO> orderRefund(OrderRefundCondition condition);
	
	/**
	 * @author liuhanning
	 * @date  2018年8月13日 下午12:48:41
	 * @Description 获取支付凭证
	 * @param condition
	 * @return
	 */
	ResponseResult<String> getprepayId(OrderPayCondition condition);
	/**
	 * @author liuhanning
	 * @date  2018年8月13日 下午12:50:13
	 * @Description 订单支付
	 * @param condition
	 * @return
	 */
	ResponseResult<OrderPayVO> orderPay(OrderPayCondition condition);
	
	/**
	 * @author liuhanning
	 * @date  2018年8月13日 下午1:07:13
	 * @Description  微信支付回调   更新订单状态
	 * @param condition
	 * @return
	 */
	Integer callbackOrderPay(OrderPayCallbackCondition condition);
	/**
	 * @author liuhanning
	 * @date  2018年8月13日 下午1:07:13
	 * @Description  微信退款回调   更新订单状态
	 * @param condition
	 * @return
	 */
	Integer callbackOrderRefund(UpdateOrderCondition condition);
	
	/**
	 * @author liuhanning
	 * @date  2018年8月15日 上午9:24:42
	 * @Description 门店资金变化(注意事项：里面的操作都是add，需要减少时传负数)
	 */
	void updateStoreBankroll(StoreBankrollChangeCondition condition);
	
	
}
