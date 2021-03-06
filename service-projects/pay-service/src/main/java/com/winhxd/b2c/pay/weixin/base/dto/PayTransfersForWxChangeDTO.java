package com.winhxd.b2c.pay.weixin.base.dto;

import lombok.Data;

/**
 * PayTransfersForWxBankDTO
 *
 * @Author yindanqing
 * @Date 2018/8/15 10:47
 * @Description: 微信转账至零钱接口入参
 */
@Data
public class PayTransfersForWxChangeDTO {

    /**
     * 商户账号appid
     */
    private String mchAppid;

    /**
     * 商户号
     */
    private String mchid;

    /**
     * 设备号
     */
    private String deviceInfo;

    /**
     * 随机字符串
     */
    private String nonceStr;

    /**
     * 签名
     */
    private String sign;

    /**
     * 商户订单号
     */
    private String partnerTradeNo;

    /**
     * 收款人唯一标识(微信openid)
     */
    private String openid;

    /**
     * NO_CHECK：不校验真实姓名,FORCE_CHECK：强校验真实姓名
     */
    private String checkName;

    /**
     * 收款方用户名
     */
    private String reUserName;

    /**
     * 提现金额,单位分
     */
    private int amount;

    /**
     * 付款说明
     */
    private String desc;

    /**
     * Ip地址
     */
    private String spbillCreateIp;

}
