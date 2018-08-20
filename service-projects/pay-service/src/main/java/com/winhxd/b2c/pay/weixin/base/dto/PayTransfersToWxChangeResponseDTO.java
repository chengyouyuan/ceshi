package com.winhxd.b2c.pay.weixin.base.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * PayTransfersToWxChangeResponseDTO
 *
 * @Author yindanqing
 * @Date 2018/8/16 21:06
 * @Description: wx转账至零钱返参
 */
@Setter
@Getter
public class PayTransfersToWxChangeResponseDTO {

    /**
     * 返回状态码
     */
    private String returnCode;

    /**
     * 返回信息
     */
    private String returnMsg;

    /**
     * 商户appid
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
     * 商户订单号
     */
    private String partnerTradeNo;

    /**
     * 微信订单号
     */
    private String paymentNo;

    /**
     * 微信支付成功时间
     */
    private String paymentTime;

}
