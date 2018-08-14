package com.winhxd.b2c.pay.weixin.service;

import com.winhxd.b2c.pay.weixin.condition.PayTransfersToWxChangeCondition;
import com.winhxd.b2c.pay.weixin.condition.PayTransfersToWxBankCondition;

/**
 * WXTransfersService
 *
 * @Author yindanqing
 * @Date 2018/8/13 12:51
 * @Description: 微信提现
 */
public interface WXTransfersService {

    /**
     * 微信提现至余额入口
     * @return
     */
    String withDrawToBalance(PayTransfersToWxChangeCondition toWxBalanceCondition);

    /**
     * 微信提现至银行卡入口
     * @return
     */
    String withDrawToBank(PayTransfersToWxBankCondition toWxBankCondition);

}
