package com.winhxd.b2c.pay.weixin.base.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 统一下单入参
 * @author mahongliang
 * @date  2018年8月15日 下午3:29:57
 * @Description 
 * @version
 */
public class PayPreOrderDTO implements Serializable {
	private static final long serialVersionUID = -6751906874587493059L;
	
	/**
     * 小程序ID
     */
    private String appid;
    
    /**
     * 商户号
     */
    private String mchId;
    
    /**
     * 随机字符串
     */
    private String nonceStr;
    
    /**
     * 签名
     */
    private String sign;
    
    /**
     * 签名类型，支持HMAC-SHA256和MD5
     */
    private String signType;
    
	/**
     * 真实订单号
     */
    private String outOrderNo;
    
    /**
     * 支付流水号
     */
    private String outTradeNo;
    
    /**
     * 订单总金额，单位为元
     */
    private BigDecimal totalAmount;
    
    /**
     * 买家用户标识
     * trade_type=JSAPI时（即公众号支付），此参数必传，此参数为微信用户在商户对应appid下的唯一标识（openid）
     */
    private String openid;
    
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

	public String getOutOrderNo() {
		return outOrderNo;
	}

	public void setOutOrderNo(String outOrderNo) {
		this.outOrderNo = outOrderNo;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getSpbillCreateIp() {
		return spbillCreateIp;
	}

	public void setSpbillCreateIp(String spbillCreateIp) {
		this.spbillCreateIp = spbillCreateIp;
	}

	public String getDeviceInfo() {
		return deviceInfo;
	}

	public void setDeviceInfo(String deviceInfo) {
		this.deviceInfo = deviceInfo;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public String getLimitPay() {
		return limitPay;
	}

	public void setLimitPay(String limitPay) {
		this.limitPay = limitPay;
	}

}
