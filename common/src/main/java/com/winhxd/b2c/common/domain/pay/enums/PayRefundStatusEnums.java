package com.winhxd.b2c.common.domain.pay.enums;

public enum PayRefundStatusEnums {

	/**
	 * 退款中
	 */
	REFUNDING((short)0,"退款中"),
	/**
	 * 退款成功
	 */
	REFUND_SUCCESS((short)1,"退款成功"),
	/**
	 * 退款失败
	 */
	REFUND_FAIL((short)2,"退款失败");
	
	
	
	private Short code;
	private String desc;
	PayRefundStatusEnums(Short code, String desc) {
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
