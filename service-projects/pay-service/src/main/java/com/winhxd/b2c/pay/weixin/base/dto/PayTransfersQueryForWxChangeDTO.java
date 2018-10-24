package com.winhxd.b2c.pay.weixin.base.dto;

import lombok.Data;

/**
 * PayTransfersQueryForWxChangeDTO
 *
 * @Author yindanqing
 * @Date 2018/8/18 11:45
 * @Description: 查询微信转账至零钱详情条件
 */
@Data
public class PayTransfersQueryForWxChangeDTO {

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
     * 商户号
     */
    private String mchId;

    /**
     * 商户号的appid
     */
    private String appid;

}
