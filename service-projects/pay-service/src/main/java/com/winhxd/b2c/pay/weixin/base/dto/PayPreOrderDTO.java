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
public class PayPreOrderDTO extends RequestBase implements Serializable {
	private static final long serialVersionUID = -6751906874587493059L;
	
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
     * 交易时间
     */
    private String timeStart;
    
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

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public int getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(int totalFee) {
		this.totalFee = totalFee;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getTimeStart() {
		return timeStart;
	}

	public void setTimeStart(String timeStart) {
		this.timeStart = timeStart;
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

	public String getFeeType() {
		return feeType;
	}

	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}

}
