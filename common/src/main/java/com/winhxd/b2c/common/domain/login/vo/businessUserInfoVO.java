package com.winhxd.b2c.common.domain.login.vo;

import java.io.Serializable;
import java.util.Date;

public class businessUserInfoVO implements Serializable {
    private Long businessId;

    private String storeName;

    private Long storeId;

    private String storeMobile;

    private String storeAddress;

    private String storeRegionCode;

    private String storePassword;

    private String shopkeeper;

    private String shopOwnerUrl;

    private Byte pickupWay;

    private Byte paymentWay;

    private String contactMobile;

    private Double lat;

    private Double lon;

    private String openid;

    private Date created;

    private Date updated;

    private String source;

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
}