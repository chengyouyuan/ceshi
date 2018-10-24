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
public class PayPreOrderCallbackDTO extends ResponseBase{
	
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
     * 支付流水号（商户订单号）
     */
    private String outTradeNo;
    
    /**
     * 订单总金额，单位为分
     */
    private Integer totalFee;
    
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
     * （非必填）附加数据，可作为自定义参数使用
     */
    private String attach;
    
    /**
     * 用户是否关注公众账号，Y-关注，N-未关注，仅在公众账号类型支付有效
     */
    private String isSubscribe;
    
    /**
     * 付款银行
     */
    private String bankType;
    
    /**
     * 应结订单金额，应结订单金额=订单金额-非充值代金券金额，应结订单金额<=订单金额。
     */
    private Integer settlementTotalFee;
    
    /**
     * 货币种类
     */
    private String feeType;
    
    /**
     * 现金支付金额
     */
    private Integer cashFee;
    
    /**
     * 现金支付货币类型
     */
    private String cashFeeType;
    
    /**
     * 总代金券金额
     */
    private Integer couponFee;
    
    /**
     * 代金券使用数量
     */
    private Integer couponCount;
    
    /**
     * 微信支付订单号
     */
    private String transactionId;
    
    /**
     * 支付完成时间
     */
    private Date timeEnd;
    
    /**
     * 主动查询返回的交易状态：
     * SUCCESS—支付成功
     * REFUND.转入退款
     * NOTPAY—未支付
     * CLOSED—已关闭
     * REVOKED—已撤销（刷卡支付）
     * USERPAYING--用户支付中
     * PAYERROR--支付失败(其他原因，如银行返回失败)
     */
    private String tradeState;
    
    /**
     * 交易状态描述
     */
    private String tradeStateDesc;

}
