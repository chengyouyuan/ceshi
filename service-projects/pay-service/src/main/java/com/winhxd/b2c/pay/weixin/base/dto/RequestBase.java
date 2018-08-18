package com.winhxd.b2c.pay.weixin.base.dto;

import java.io.Serializable;

/**
 * 请求公共参数
 * @author mahongliang
 * @date  2018年8月18日 下午3:23:17
 * @Description 
 * @version
 */
public class RequestBase implements Serializable {
	private static final long serialVersionUID = -5387326139912673648L;
	
	/**
	 * 小程序ID
	 */
	private String appid;
	
	/**
	 * 商户号
	 */
	private String mchId;
	
	/**
	 * 设备号
	 */
	private String deviceInfo;
	
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

	public String getDeviceInfo() {
		return deviceInfo;
	}

	public void setDeviceInfo(String deviceInfo) {
		this.deviceInfo = deviceInfo;
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
