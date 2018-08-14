package com.winhxd.b2c.pay.weixin.service.withdraw.impl;

import com.winhxd.b2c.pay.weixin.base.condition.PayTransfersToWxBalanceCondition;
import com.winhxd.b2c.pay.weixin.base.condition.PayTransfersToWxBankCondition;
import com.winhxd.b2c.pay.weixin.service.withdraw.WXWithDrawService;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

/**
 * WXWithDrawServiceImpl
 *
 * @Author yindanqing
 * @Date 2018/8/13 12:47
 * @Description: 微信提现实现
 */
@Service(value = "wxWithDrawService")
public class WXWithDrawServiceImpl implements WXWithDrawService {

    @Override
    public String withDrawToBalance(PayTransfersToWxBalanceCondition toWxBalanceCondition) {
        checkNecessaryFieldForBalance();
        getReqParamForBalance();
        return null;
    }

    @Override
    public String withDrawToBank(PayTransfersToWxBankCondition toWxBankCondition) {
        checkNecessaryFieldForBank();
        getReqParamForBank();
        return null;
    }

    /**
     * 检查必须字段
     * @return
     */
    private boolean checkNecessaryFieldForBalance(){
        return false;
    }

    /**
     * 检查必须字段
     * @return
     */
    private boolean checkNecessaryFieldForBank(){
        return false;
    }

    /**
     * 获得请求参数
     * @return
     */
    private Document getReqParamForBalance(){
        return null;
    }

    /**
     * 获得请求参数
     * @return
     */
    private Document getReqParamForBank(){
        return null;
    }

}
