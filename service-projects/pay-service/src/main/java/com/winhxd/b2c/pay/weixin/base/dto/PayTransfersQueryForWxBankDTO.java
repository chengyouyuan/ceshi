package com.winhxd.b2c.pay.weixin.base.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * PayTransfersQueryForWxChangeDTO
 *
 * @Author yindanqing
 * @Date 2018/8/18 11:45
 * @Description: 查询微信转账至银行卡详情条件
 */
@Setter
@Getter
public class PayTransfersQueryForWxBankDTO {

    /**
     * 商户号
     */
    private String mchId;

    /**
     * 商户订单号
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

}
