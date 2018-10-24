package com.winhxd.b2c.pay.weixin.base.dto;

import lombok.Data;

/**
 * PayTransfersForWxBankDTO
 *
 * @Author yindanqing
 * @Date 2018/8/15 10:47
 * @Description: 微信转账至银行卡接口入参
 */
@Data
public class PayTransfersForWxBankDTO {

    /**
     * 商户号
     */
    private String mchId;

    /**
     * 商户企业付款单号
     */
    private String partnerTradeNo;

    /**
     * 随机字符串
     */
    private String nonceStr;

    /**
     * 签名
     */
    private String sign;

    /**
     * 收款方银行卡号
     */
    private String encBankNo;

    /**
     * 收款方用户名
     */
    private String encTrueName;

    /**
     * 收款方开户行
     */
    private String bankCode;

    /**
     * 提现金额,单位分
     */
    private int amount;

    /**
     * 付款说明
     */
    private String desc;

}
