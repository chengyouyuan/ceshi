package com.winhxd.b2c.pay.weixin.base.dto;

import lombok.Data;

import java.util.Date;

/**
 * 统一下单入参
 * @author mahongliang
 * @date  2018年8月15日 下午3:29:57
 * @Description 
 * @version
 */
@Data
public class PayPreOrderDTO extends RequestBase{
	
    /**
     * 支付流水号
     */
    private String outTradeNo;
    
    /**
     * 订单总金额，单位为分
     */
    private int totalFee;
    
    /**
     * 买家用户标识
     * trade_type=JSAPI时（即公众号支付），此参数必传，此参数为微信用户在商户对应appid下的唯一标识（openid）
     */
    private String openid;
    
    /**
     * 交易类型，小程序取值如下：JSAPI
     */
    private String tradeType;
    
    /**
     * 交易时间
     */
    private Date timeStart;
    
    /**
     * 商品描述
     */
    private String body;
    
    /**
     * 终端IP
     */
    private String spbillCreateIp;
	
	/**
     * （非必填）设备号
     */
    private String deviceInfo;
    
    /**
     * （非必填）商品ID
     */
    private String productId;

    /**
     * （非必填）附加数据，可作为自定义参数使用
     */
    private String attach;
    
    /**
     * （非必填）
     * 限制支付类型（微信参数），上传此参数no_credit--可限制用户不能使用信用卡支付
     */
    private String limitPay;
    
    /**
     * （非必填）标价币种
     */
    private String feeType;

	/**
	 * 支付结果通知URL
	 */
	private String notifyUrl;

}
