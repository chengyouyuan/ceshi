package com.winhxd.b2c.pay.weixin.service.withdraw;

import com.winhxd.b2c.pay.weixin.base.condition.PayTransfersToWxBalanceCondition;
import com.winhxd.b2c.pay.weixin.base.condition.PayTransfersToWxBankCondition;

/**
 * WXWithDrawService
 *
 * @Author yindanqing
 * @Date 2018/8/13 12:51
 * @Description: 微信提现
 */
public interface WXWithDrawService {

    /**
     * 微信提现至余额入口
     * @return
     */
    String withDrawToBalance(PayTransfersToWxBalanceCondition toWxBalanceCondition);

    /**
     * 微信提现至银行卡入口
     * @return
     */
    String withDrawToBank(PayTransfersToWxBankCondition toWxBankCondition);

}
