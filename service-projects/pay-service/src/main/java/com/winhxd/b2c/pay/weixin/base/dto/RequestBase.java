package com.winhxd.b2c.pay.weixin.base.dto;

/**
 * 请求公共参数
 * @author mahongliang
 * @date  2018年8月18日 下午3:23:17
 * @Description 
 * @version
 */
public class RequestBase {
	
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
	 * 签名类型，默认为MD5，支持HMAC-SHA256和MD5
	 */
	private String signType;

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

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
