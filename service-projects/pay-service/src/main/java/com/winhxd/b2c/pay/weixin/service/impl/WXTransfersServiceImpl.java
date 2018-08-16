package com.winhxd.b2c.pay.weixin.service.impl;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.pay.condition.PayTransfersToWxBankCondition;
import com.winhxd.b2c.common.domain.pay.condition.PayTransfersToWxChangeCondition;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.pay.weixin.base.wxpayapi.WXPay;
import com.winhxd.b2c.pay.weixin.base.wxpayapi.WXPayConfig;
import com.winhxd.b2c.pay.weixin.base.wxpayapi.WXPayUtil;
import com.winhxd.b2c.pay.weixin.base.dto.PayTransfersForWxBankDTO;
import com.winhxd.b2c.pay.weixin.base.dto.PayTransfersForWxChangeDTO;
import com.winhxd.b2c.pay.weixin.service.WXTransfersService;
import com.winhxd.b2c.pay.weixin.util.BeanAndXmlUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

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

    /**
     * 默认设置姓名强校验
     */
    private static final String FORCE_CHECK = "FORCE_CHECK";

    /**
     * 分与元单位转换
     */
    private static final BigDecimal UNITS = new BigDecimal("100");

    @Autowired
    private WXPay wxPay;

    @Autowired
    private WXPayConfig wxPayConfig;

    @Override
    public String transfersToChange(PayTransfersToWxChangeCondition toWxBalanceCondition) {
        try {
            if (checkNecessaryFieldForChange(toWxBalanceCondition)) {
                logger.warn("转账必填字段为空");
                throw new BusinessException(BusinessCode.CODE_600201);
            }
            PayTransfersForWxChangeDTO wxChangeDTO = getReqParamForChange(toWxBalanceCondition);
            String respxml = wxPay.transferToChange(BeanAndXmlUtil.beanToSortedMap(wxChangeDTO));
        } catch (Exception ex) {
            logger.error("TransfersToChange error.");
        }
        return null;
    }

    @Override
    public String transfersToBank(PayTransfersToWxBankCondition toWxBankCondition) {
        try {
            if (checkNecessaryFieldForBank(toWxBankCondition)) {
                logger.warn("转账必填字段为空");
                throw new BusinessException(BusinessCode.CODE_600201);
            }
            PayTransfersForWxBankDTO wxBankDTO = getReqParamForBank(toWxBankCondition);
            //接收返参
            String respxml = wxPay.transferToBank(BeanAndXmlUtil.beanToSortedMap(wxBankDTO));
        } catch (Exception ex) {
            logger.error("TransfersToBank error.");
        }
        return null;
    }

    /**
     * 检查必须字段
     * @Author yindanqing
     * @Date 2018-8-15 10:24:48
     */
    private boolean checkNecessaryFieldForChange(PayTransfersToWxChangeCondition toWxBalanceCondition){
        boolean res = //StringUtils.isBlank(toWxBalanceCondition.getMchAppid()) ||
                StringUtils.isBlank(toWxBalanceCondition.getPartnerTradeNo())
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
    private PayTransfersForWxChangeDTO getReqParamForChange(PayTransfersToWxChangeCondition toWxBalanceCondition) throws Exception {
        PayTransfersForWxChangeDTO forWxChangeDTO = new PayTransfersForWxChangeDTO();
        forWxChangeDTO.setMchAppid(wxPayConfig.getMchAppID());
        forWxChangeDTO.setMchid(wxPayConfig.getMchID());
        forWxChangeDTO.setDeviceInfo(toWxBalanceCondition.getDeviceInfo());
        forWxChangeDTO.setNonceStr(WXPayUtil.generateNonceStr());
        forWxChangeDTO.setPartnerTradeNo(toWxBalanceCondition.getPartnerTradeNo());
        forWxChangeDTO.setOpenid(toWxBalanceCondition.getAccountId());
        forWxChangeDTO.setCheckName(FORCE_CHECK);
        forWxChangeDTO.setReUserName(toWxBalanceCondition.getAccountName());
        forWxChangeDTO.setAmount(toWxBalanceCondition.getTotalAmount().multiply(UNITS).intValue());
        forWxChangeDTO.setDesc(toWxBalanceCondition.getDesc());
        forWxChangeDTO.setSpbillCreateIp(toWxBalanceCondition.getSpbillCreateIp());
        //设置签名
        forWxChangeDTO.setSign(WXPayUtil.generateSignature(BeanAndXmlUtil.beanToSortedMap(forWxChangeDTO), wxPayConfig.getKey()));
        return forWxChangeDTO;
    }

    /**
     * 获得请求参数
     * @return
     */
    private PayTransfersForWxBankDTO getReqParamForBank(PayTransfersToWxBankCondition toWxBankCondition) throws Exception {
        PayTransfersForWxBankDTO forWxBankDTO = new PayTransfersForWxBankDTO();
        forWxBankDTO.setMchid(wxPayConfig.getMchID());
        forWxBankDTO.setPartnerTradeNo(toWxBankCondition.getPartnerTradeNo());
        forWxBankDTO.setNonceStr(WXPayUtil.generateNonceStr());
        //处理卡号姓名加密rsa
        forWxBankDTO.setEncBankNo(toWxBankCondition.getAccount());
        forWxBankDTO.setEncTrueName(toWxBankCondition.getAccountName());
        forWxBankDTO.setBankCode(String.valueOf(toWxBankCondition.getChannelCode().getCode()));
        forWxBankDTO.setAmount(toWxBankCondition.getTotalAmount().multiply(UNITS).intValue());
        forWxBankDTO.setDesc(toWxBankCondition.getDesc());
        //处理签名
        forWxBankDTO.setSign(WXPayUtil.generateSignature(BeanAndXmlUtil.beanToSortedMap(toWxBankCondition), wxPayConfig.getKey()));
        return forWxBankDTO;
    }

    public static void main(String[] args) throws Exception {
        WXPay wxPay = new WXPay();
        SortedMap<String,String> sortedMap = new TreeMap<String,String>();
        sortedMap.put("mch_id", "1467361502");
        sortedMap.put("nonce_str", WXPayUtil.generateNonceStr());
        sortedMap.put("sign_type", "MD5");
        sortedMap.put("sign", WXPayUtil.generateSignature(sortedMap, "u29K8cDc48zhF0hKlQ3pd1tiamkZpRoS"));
        System.out.println(wxPay.publicKey(sortedMap));
    }

}
