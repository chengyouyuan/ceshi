package com.winhxd.b2c.common.domain.pay.enums;

public enum StoreBankRollOpearateEnums {
	/**
	 * 订单完成操作
	 */
	ORDER_FINISH(1,"订单完成"),
	/**
	 * 结算审核操作
	 */
	SETTLEMENT_AUDIT(2,"结算审核"),
	/**
	 * 提现申请操作
	 */
	WITHDRAWALS_APPLY(3,"提现申请"),
	
	/**
	 * 提现成功操作
	 */
	WITHDRAWALS_SUCCESS(4,"提现成功"),
	/**
	 * 提现审核不通过操作
	 */
	WITHDRAWALS_AUDIT_NOT_PASS(5,"提现审核不通过"),
	/**
	 * 提现审核不通过操作
	 */
	WITHDRAWALS_FAIL(6,"提现失败");
	
	private Integer code;
	private String desc;
	
	StoreBankRollOpearateEnums(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
}
