package com.winhxd.b2c.common.domain.system.login.model;

import java.io.Serializable;
import java.math.BigDecimal;

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
    /**
     * 门头照
     */
    private String storePicImg;
    /**
     * poiCode
     */
    private BigDecimal poiCode ;
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
	public String getStorePicImg() {
		return storePicImg;
	}
	public void setStorePicImg(String storePicImg) {
		this.storePicImg = storePicImg;
	}
	public BigDecimal getPoiCode() {
		return poiCode;
	}
	public void setPoiCode(BigDecimal poiCode) {
		this.poiCode = poiCode;
	}
    
}