package com.winhxd.b2c.pay.weixin.service.impl;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.pay.weixin.condition.PayTransfersToWxChangeCondition;
import com.winhxd.b2c.pay.weixin.condition.PayTransfersToWxBankCondition;
import com.winhxd.b2c.pay.weixin.service.WXTransfersService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

/**
 * WXTransfersServiceImpl
 *
 * @Author yindanqing
 * @Date 2018/8/13 12:47
 * @Description: 微信转账实现
 */
@Service(value = "wXTransfersService")
public class WXTransfersServiceImpl implements WXTransfersService {

    private static final Logger logger = LoggerFactory.getLogger(WXTransfersServiceImpl.class);

    @Override
    public String transfersToChange(PayTransfersToWxChangeCondition toWxBalanceCondition) {
        if(checkNecessaryFieldForChange(toWxBalanceCondition)){
            logger.warn("转账必填字段为空");
            throw new BusinessException(BusinessCode.CODE_600201);
        }
        getReqParamForChange();
        return null;
    }

    @Override
    public String transfersToBank(PayTransfersToWxBankCondition toWxBankCondition) {
        if(checkNecessaryFieldForBank(toWxBankCondition)){
            logger.warn("转账必填字段为空");
            throw new BusinessException(BusinessCode.CODE_600201);
        }
        getReqParamForBank();
        return null;
    }

    /**
     * 检查必须字段
     * @Author yindanqing
     * @Date 2018-8-15 10:24:48
     */
    private boolean checkNecessaryFieldForChange(PayTransfersToWxChangeCondition toWxBalanceCondition){
        boolean res = StringUtils.isBlank(toWxBalanceCondition.getPartnerTradeNo())
                || StringUtils.isBlank(toWxBalanceCondition.getAccountId())
                || StringUtils.isBlank(toWxBalanceCondition.getAccountName())
                || null == toWxBalanceCondition.getTotalAmount()
                && toWxBalanceCondition.getTotalAmount().doubleValue() <= 0d
                || StringUtils.isBlank(toWxBalanceCondition.getDesc())
                || StringUtils.isBlank(toWxBalanceCondition.getSpbillCreateIp());
        return res;
    }

    /**
     * 检查必须字段
     * @Author yindanqing
     * @Date 2018-8-15 10:24:48
     */
    private boolean checkNecessaryFieldForBank(PayTransfersToWxBankCondition toWxBankCondition){
        boolean res = StringUtils.isBlank(toWxBankCondition.getPartnerTradeNo())
                || StringUtils.isBlank(toWxBankCondition.getAccount())
                || StringUtils.isBlank(toWxBankCondition.getAccountName())
                || null == toWxBankCondition.getChannelCode()
                || null == toWxBankCondition.getTotalAmount()
                || toWxBankCondition.getTotalAmount().doubleValue() <= 0d
                || StringUtils.isBlank(toWxBankCondition.getDesc());
        return res;
    }

    /**
     * 获得请求参数
     * @return
     */
    private Document getReqParamForChange(){
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
