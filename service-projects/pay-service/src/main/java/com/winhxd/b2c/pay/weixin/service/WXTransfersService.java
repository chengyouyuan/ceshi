package com.winhxd.b2c.pay.weixin.service;

import com.winhxd.b2c.common.domain.pay.condition.PayTransfersToWxBankCondition;
import com.winhxd.b2c.common.domain.pay.condition.PayTransfersToWxChangeCondition;
import com.winhxd.b2c.common.domain.pay.vo.PayTransfersToWxBankVO;
import com.winhxd.b2c.common.domain.pay.vo.PayTransfersToWxChangeVO;

/**
 * WXTransfersService
 *
 * @Author yindanqing
 * @Date 2018/8/13 12:51
 * @Description: 微信转账
 */
public interface WXTransfersService {

    /**
     * 微信提现至余额入口
     * @return
     */
    PayTransfersToWxChangeVO transfersToChange(PayTransfersToWxChangeCondition toWxBalanceCondition);

    /**
     * 微信提现至银行卡入口
     * @return
     */
    PayTransfersToWxBankVO transfersToBank(PayTransfersToWxBankCondition toWxBankCondition);

}
