package com.winhxd.b2c.message.sms.enums;

/**
 * 短信发送供应商枚举
 */
public enum SmsSupplierEnum {

	/**
	 * 发送平台枚举
	 */
	DEFUALT("yxt", "yingxt1", "Tch456789", "http://222.73.117.158/msg/", null, "创蓝短信平台混合通道"),

	MANDAOI("yxt", "yingxt1", "Tch456789", "http://222.73.117.158/msg/", null, "创蓝短信平台混合通道"),
	mandaoRetail("mandaoRetail", "mandaoRetail", "Tch456789", "http://222.73.117.158/msg/", null, "漫道短信平台惠下单专用通道"),
	/**
	 * 创蓝营销通道SmsConstant.getMarketingUrl()
	 */
	marketing_cl("yxt", "yingxt1yx", "Tch456789", "http://222.73.117.169/msg/", null, "创蓝短信平台营销通道"),

	/**
	 * 创蓝混合通道SmsConstant.getURL()
	 */
	mixed_cl("yxt", "yingxt1", "Tch456789", "http://222.73.117.158/msg/", DEFUALT, "创蓝短信平台混合通道"),

	/**
	 * 创蓝验证码专用通道SmsConstant.getVerificationUrl(),
	 */
	verification_cl("yxt", "VIP-yxt666", "As888888", "http://222.73.117.146/msg/", mixed_cl, "创蓝短信平台验证码通道"),

	/**
	 * 阅信文字验证码通道SmsConstant.getYXVerificationUrl()
	 */
	verification_yx("yx", "hytx01", "0d58c19fd7fee5e9c204ca67081617d0", "http://60.205.14.180:9000/HttpSmsMt", verification_cl, "阅信短信平台文字验证码通道"),


	/**
	 * 阅信语音验证码通道SmsConstant.getYXVerificationUrl(),
	 */
	voice_yx("yx", "hytx02", "60a3efb668bbab524fd8911630fc2ecd", "http://60.205.14.180:9000/HttpSmsMt", verification_cl, "阅信短信平台语音验证码通道"),

	/**
	 * 创蓝语音验证码通道SmsConstant.getURL()
	 */
	voice_cl("yxt", "YYyxt666", "Tch456789", "http://222.73.117.158/msg/", voice_yx, "创蓝短信平台语音验证码通道"),

	international_cl("yxt", "I3345403", "twOg9WVT3Zb69c", "https://intapi.253.com", null, "创蓝国际短信");

	private String code;// 供应商代码

	private String url;// 请求地址

	private String account;//  账号

	private String pwd;// 密码

	private SmsSupplierEnum backup;// 备胎

	private String remark;// 描述

	private SmsSupplierEnum(String code, String account, String pwd, String url, SmsSupplierEnum backup, String remark) {
		this.code = code;
		this.remark = remark;
		this.url = url;
		this.account = account;
		this.pwd = pwd;
		this.backup = backup;
	}

	public String getCode() {
		return code;
	}

	public String getRemark() {
		return remark;
	}

	public String getUrl() {
		return url;
	}

	public String getAccount() {
		return account;
	}

	public String getPwd() {
		return pwd;
	}

	public SmsSupplierEnum getBackup() {
		return backup;
	}
}
