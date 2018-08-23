package com.winhxd.b2c.pay.weixin.service;

import com.winhxd.b2c.common.domain.pay.condition.PayTransfersToWxBankCondition;
import com.winhxd.b2c.common.domain.pay.condition.PayTransfersToWxChangeCondition;
import com.winhxd.b2c.common.domain.pay.vo.PayTransfersQueryToWxBankVO;
import com.winhxd.b2c.common.domain.pay.vo.PayTransfersToWxBankVO;
import com.winhxd.b2c.common.domain.pay.vo.PayTransfersToWxChangeVO;
import com.winhxd.b2c.pay.weixin.base.dto.PayTransfersQueryForWxBankResponseDTO;

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
     * @param toWxBalanceCondition 条件
     * @return
     */
    PayTransfersToWxChangeVO transfersToChange(PayTransfersToWxChangeCondition toWxBalanceCondition);

    /**
     * 微信提现至银行卡入口
     * @param toWxBankCondition 条件
     * @return
     * @return
     */
    PayTransfersToWxBankVO transfersToBank(PayTransfersToWxBankCondition toWxBankCondition);

    /**
     * 重新查询转账结果, 确认结果
     *
     * @param partnerTradeNo wx转账至银行卡接口返参
     * @return
     * @throws Exception
     */
    PayTransfersQueryToWxBankVO getExactResultForWxBank(String partnerTradeNo) throws Exception;

}
