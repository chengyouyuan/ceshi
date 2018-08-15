package com.winhxd.b2c.pay.weixin.condition;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 预支付参数
 * @author mahongliang
 * @date  2018年8月15日 上午11:28:49
 * @Description 
 * @version
 */
public class PayPreOrderCondition implements Serializable {
	private static final long serialVersionUID = -6751906874587493059L;
	
	/**
     * 真实订单号
     */
    private String outOrderNo;
    
    /**
     * 订单总金额，单位为元
     */
    private BigDecimal totalAmount;
    
    /**
     * （非必填）商品描述
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
     * 买家用户标识
     * trade_type=JSAPI时（即公众号支付），此参数必传，此参数为微信用户在商户对应appid下的唯一标识（openid）
     */
    private String openid;

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

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

}
