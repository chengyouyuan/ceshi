package com.winhxd.b2c.common.context;

/**
 * 门店用户数据
 */
public class StoreUser {
	private Long businessId;
	private Long storeCustomerId;
	public Long getBusinessId() {
		return businessId;
	}
	public void setBusinessId(Long businessId) {
		this.businessId = businessId;
	}
	public Long getStoreCustomerId() {
		return storeCustomerId;
	}
	public void setStoreCustomerId(Long storeCustomerId) {
		this.storeCustomerId = storeCustomerId;
	}
}