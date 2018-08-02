package com.winhxd.b2c.common.domain.login.model;

import java.io.Serializable;

import java.util.Date;
/**
 * @author wufuyun
 * @date  2018年8月2日 下午3:43:57
 * @Description b端用户信息
 * @version
 */
public class businessUserInfo implements Serializable {
	/**
     * 主键
     */
	private Long id;
	/**
     * 门店用户id
     */
    private Long businessId;
    /**
     * 门店名称
     */
    private String storeName;
    /**
     * 门店编码
     */
    private Long storeId;
    /**
     * 用户账号
     */
    private String storeMobile;
    /**
     * 门店地址
     */
    private String storeAddress;
    /**
     * 区域编码
     */
    private String storeRegionCode;
    /**
     * 密码
     */
    private String storePassword;
    /**
     * 店主名称
     */
    private String shopkeeper;
    /**
     * 店主头像
     */
    private String shopOwnerUrl;
    /**
     * 取货方式（1、自提）
     */
    private Byte pickupWay;
    /**
     * 支付方式（1、微信在线付款2、微信扫码付款）
     */
    private Byte paymentWay;
    /**
     * 联系方式 
     */
    private String contactMobile;
    /**
     * 纬度
     */
    private Double lat;
    /**
     * 经度
     */
    private Double lon;
    /**
     * 微信openid
     */
    private String openid;

    private Date created;

    private Date updated;
    /**
     * 来源
     */
    private String source;
    /**
     * 0有效，1无效
     */
    private Byte storeStatus;

    private static final long serialVersionUID = 1L;

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName == null ? null : storeName.trim();
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public String getStoreMobile() {
        return storeMobile;
    }

    public void setStoreMobile(String storeMobile) {
        this.storeMobile = storeMobile == null ? null : storeMobile.trim();
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress == null ? null : storeAddress.trim();
    }

    public String getStoreRegionCode() {
        return storeRegionCode;
    }

    public void setStoreRegionCode(String storeRegionCode) {
        this.storeRegionCode = storeRegionCode == null ? null : storeRegionCode.trim();
    }

    public String getStorePassword() {
        return storePassword;
    }

    public void setStorePassword(String storePassword) {
        this.storePassword = storePassword == null ? null : storePassword.trim();
    }

    public String getShopkeeper() {
        return shopkeeper;
    }

    public void setShopkeeper(String shopkeeper) {
        this.shopkeeper = shopkeeper == null ? null : shopkeeper.trim();
    }

    public String getShopOwnerUrl() {
        return shopOwnerUrl;
    }

    public void setShopOwnerUrl(String shopOwnerUrl) {
        this.shopOwnerUrl = shopOwnerUrl == null ? null : shopOwnerUrl.trim();
    }

    public Byte getPickupWay() {
        return pickupWay;
    }

    public void setPickupWay(Byte pickupWay) {
        this.pickupWay = pickupWay;
    }

    public Byte getPaymentWay() {
        return paymentWay;
    }

    public void setPaymentWay(Byte paymentWay) {
        this.paymentWay = paymentWay;
    }

    public String getContactMobile() {
        return contactMobile;
    }

    public void setContactMobile(String contactMobile) {
        this.contactMobile = contactMobile == null ? null : contactMobile.trim();
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid == null ? null : openid.trim();
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source == null ? null : source.trim();
    }

    public Byte getStoreStatus() {
        return storeStatus;
    }

    public void setStoreStatus(Byte storeStatus) {
        this.storeStatus = storeStatus;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
    
}