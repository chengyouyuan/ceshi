package com.winhxd.b2c.pay.weixin.base.condition;

import java.io.Serializable;

public class RequestBase implements Serializable {
	private static final long serialVersionUID = 2129736072898667357L;
	
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
	 * 签名类型
	 */
	private String signType;

	public String getMchId() {
		return mchId;
	}

	public void setMchId(String mchId) {
		this.mchId = mchId;
	}

	public String getNonceStr() {
		return nonceStr;
	}

	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}
	
}
