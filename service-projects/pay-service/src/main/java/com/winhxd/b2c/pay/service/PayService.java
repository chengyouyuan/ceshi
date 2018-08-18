package com.winhxd.b2c.pay.service;

import java.util.List;

import com.winhxd.b2c.common.domain.pay.condition.PayPreOrderCondition;
import com.winhxd.b2c.common.domain.pay.condition.PayRefundCondition;
import com.winhxd.b2c.common.domain.pay.condition.PayTransfersToWxBankCondition;
import com.winhxd.b2c.common.domain.pay.condition.PayTransfersToWxChangeCondition;
import com.winhxd.b2c.common.domain.pay.condition.StoreBindStoreWalletCondition;
import com.winhxd.b2c.common.domain.pay.condition.UpdateStoreBankRollCondition;
import com.winhxd.b2c.common.domain.pay.model.PayStoreWallet;
import com.winhxd.b2c.common.domain.pay.vo.PayPreOrderVO;
import com.winhxd.b2c.common.domain.pay.vo.PayRefundVO;
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
	Integer callbackOrderPay(PayBill condition);
	/**
	 * @author liuhanning
	 * @date  2018年8月13日 下午1:07:13
	 * @Description  微信退款回调   更新订单状态
	 * @param condition
	 * @return
	 */
	Integer callbackOrderRefund(PayRefund condition);
	
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
	PayPreOrderVO unifiedOrder(PayPreOrderCondition condition);
	
	/**
	 * 退款
	 * @author mahongliang
	 * @date  2018年8月16日 上午10:04:54
	 * @Description 
	 * @param payRefund
	 * @return
	 */
	PayRefundVO refundOrder(PayRefundCondition payRefund);
	
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
     * @author liuhanning
     * @date  2018年8月16日 下午8:40:14
     * @Description 获取提现钱包
     * @param storeId
     * @return
     */
    List<PayStoreWallet> selectPayStoreWalletByStoreId();
    
    /**
     * @author liuhanning
     * @date  2018年8月17日 上午10:46:26
     * @Description 门店绑定微信钱包
     * @param condition
     */
    void storeBindStoreWallet(StoreBindStoreWalletCondition condition);
	
}
