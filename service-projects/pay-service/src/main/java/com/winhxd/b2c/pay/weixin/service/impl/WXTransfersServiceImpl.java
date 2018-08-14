package com.winhxd.b2c.pay.weixin.service.impl;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.pay.weixin.condition.PayTransfersToWxChangeCondition;
import com.winhxd.b2c.pay.weixin.condition.PayTransfersToWxBankCondition;
import com.winhxd.b2c.pay.weixin.service.WXTransfersService;
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
        if(checkNecessaryFieldForChange()){
            logger.warn("转账必填字段为空");
            throw new BusinessException(BusinessCode.CODE_600201);
        }
        getReqParamForChange();
        return null;
    }

    @Override
    public String transfersToBank(PayTransfersToWxBankCondition toWxBankCondition) {
        if(checkNecessaryFieldForBank()){
            logger.warn("转账必填字段为空");
            throw new BusinessException(BusinessCode.CODE_600201);
        }
        getReqParamForBank();
        return null;
    }

    /**
     * 检查必须字段
     * @return
     */
    private boolean checkNecessaryFieldForChange(){
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
