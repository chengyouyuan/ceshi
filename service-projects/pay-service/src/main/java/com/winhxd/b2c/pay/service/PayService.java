package com.winhxd.b2c.pay.service;

import java.util.List;

import com.winhxd.b2c.common.domain.order.model.OrderInfo;
import com.winhxd.b2c.common.domain.pay.condition.*;
import com.winhxd.b2c.common.domain.pay.model.PayStoreWallet;
import com.winhxd.b2c.common.domain.pay.vo.OrderPayVO;
import com.winhxd.b2c.pay.weixin.model.PayBill;
import com.winhxd.b2c.pay.weixin.model.PayRefund;

/**
 * @author liuhanning
 * @date  2018年8月11日 下午4:09:33
 * @Description 支付service
 * @version 
 */
public interface PayService {

	
	
	/**
	 * @author liuhanning
	 * @date  2018年8月13日 下午1:07:13
	 * @Description  微信支付回调   更新订单状态
	 * @param condition
	 * @return
	 */
	Boolean callbackOrderPay(PayBill condition);
	/**
	 * @author liuhanning
	 * @date  2018年8月13日 下午1:07:13
	 * @Description  微信退款回调   更新订单状态
	 * @param condition
	 * @return
	 */
	Boolean callbackOrderRefund(PayRefund condition);
	
	/**
	 * @author liuhanning
	 * @date  2018年8月15日 上午9:24:42
	 * @Description 门店资金变化(注意：里面的操作都是add，需要减少时传负数)
	 */
	void updateStoreBankroll(UpdateStoreBankRollCondition condition);
	

	
	/**
	 * 小程序预支付
	 * @author mahongliang
	 * @date  2018年8月16日 上午10:04:17
	 * @Description 
	 * @param condition
	 * @return
	 */
	OrderPayVO unifiedOrder(PayPreOrderCondition condition);
	
	/**
	 * 
	 * @author 刘寒宁
	 * @date  2018年8月16日 上午10:04:54
	 * @Description 退款事件处理
	 * @return
	 */
	void refundOrder(String orderNo, OrderInfo order);
	
	/**
	 * @author 刘寒宁
	 * @date  2018年8月16日 上午10:04:54
	 * @Description 订单完成事件处理
	 * @return
	 */
	 void orderFinishHandler(String orderNo, OrderInfo orderInfo);
	
	/**
     * 微信提现至余额入口
     * @return
     */
    int transfersToChange(PayTransfersToWxChangeCondition toWxBalanceCondition);

    /**
     * 微信提现至银行卡入口
     * @return
     */
    int transfersToBank(PayTransfersToWxBankCondition toWxBankCondition);

	/**
	 * 微信提现公共接口
	 * @return
	 */
	int transfersPatrent(PayWithdrawalsCondition toWxBankCondition);
    
    /**
     * @author liuhanning
     * @date  2018年8月16日 下午8:40:14
     * @Description 获取提现钱包
     * @param storeId
     * @return
     */
    List<PayStoreWallet> selectPayStoreWalletByStoreId();
    
    /**
     * @author liuhanning
     * @date  2018年8月20日 上午11:09:58
     * @Description 查询订单发起过支付
     * @param condition
     * @return
     */
    Boolean orderIsPay(OrderIsPayCondition condition);

	/**
	 * 轮询确认转账到银行卡记录状态
	 * @Author yindanqing
	 * @Date 2018-8-22 12:50:29
	 * @return 更新状态计数
	 */
	Integer confirmTransferToBankStatus() throws Exception;

}
