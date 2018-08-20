package com.winhxd.b2c.common.domain.pay.enums;

public enum PayStatusEnums {

	/**
	 * 支付中
	 */
	PAYING((short)0,"支付中"),
	/**
	 * 支付成功
	 */
	PAY_SUCCESS((short)1,"支付成功"),
	/**
	 * 支付失败
	 */
	PAY_FAIL((short)2,"支付失败");
	
	
	
	private Short code;
	private String desc;
	PayStatusEnums(Short code, String desc) {
        this.code = code;
        this.desc = desc;
    }
	public Short getCode() {
		return code;
	}
	public void setCode(Short code) {
		this.code = code;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	
}
