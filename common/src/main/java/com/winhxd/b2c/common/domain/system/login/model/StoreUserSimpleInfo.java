package com.winhxd.b2c.common.domain.system.login.model;

import java.io.Serializable;

public class StoreUserSimpleInfo implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
     * 惠下单门店用户id(云平台crm_ws_customer表的customer_id)
     */
    private Long storeCustomerId;
    /**
     * 用户账号
     */
    private String storeMobile;
    /**
     * 区域编码
     */
    private String storeRegionCode;
	public Long getStoreCustomerId() {
		return storeCustomerId;
	}
	public void setStoreCustomerId(Long storeCustomerId) {
		this.storeCustomerId = storeCustomerId;
	}
	public String getStoreMobile() {
		return storeMobile;
	}
	public void setStoreMobile(String storeMobile) {
		this.storeMobile = storeMobile;
	}
	public String getStoreRegionCode() {
		return storeRegionCode;
	}
	public void setStoreRegionCode(String storeRegionCode) {
		this.storeRegionCode = storeRegionCode;
	}
    
}