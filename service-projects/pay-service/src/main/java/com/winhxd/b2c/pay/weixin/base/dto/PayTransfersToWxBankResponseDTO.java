package com.winhxd.b2c.pay.weixin.base.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * PayTransfersToWxBankResponseDTO
 *
 * @Author yindanqing
 * @Date 2018/8/16 21:38
 * @Description: wx转账至银行卡返参
 */
@Setter
@Getter
public class PayTransfersToWxBankResponseDTO {

    /**
     * 返回状态码
     */
    private String returnCode;

    /**
     * 返回信息
     */
    private String returnMsg;

    /**
     * 业务结果
     */
    private String resultCode;

    /**
     * 错误代码
     */
    private String errCode;

    /**
     * 错误代码描述
     */
    private String errCodeDes;

    /**
     * 商户号
     */
    private String mchId;

    /**
     * 商户订单号
     */
    private String partnerTradeNo;

    /**
     * 代付金额
     */
    private Integer amount;

    /**
     * 随机字符串
     */
    private String nonceStr;

    /**
     * 签名
     */
    private String sign;

    /**
     * 微信企业付款单号
     */
    private String paymentNo;

    /**
     * 手续费金额
     */
    private Integer cmmsAmt;

    @Override
    public String toString() {
        return "PayTransfersToWxBankResponseDTO{" +
                "returnCode='" + returnCode + '\'' +
                ", returnMsg='" + returnMsg + '\'' +
                ", resultCode='" + resultCode + '\'' +
                ", errCode='" + errCode + '\'' +
                ", errCodeDes='" + errCodeDes + '\'' +
                ", mchId='" + mchId + '\'' +
                ", partnerTradeNo='" + partnerTradeNo + '\'' +
                ", amount=" + amount +
                ", nonceStr='" + nonceStr + '\'' +
                ", sign='" + sign + '\'' +
                ", paymentNo='" + paymentNo + '\'' +
                ", cmmsAmt=" + cmmsAmt +
                '}';
    }
}
