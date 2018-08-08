package com.winhxd.b2c.common.domain.system.login.model;

import java.io.Serializable;
import java.util.Date;
/**
 * @author wufuyun
 * @date  2018年8月2日 下午3:43:57
 * @Description b端用户信息
 * @version
 */
public class StoreUserInfo implements Serializable {
	/**
     * 主键
     */
	private Long id;
    /**
     * 门店名称
     */
    private String storeName;
    /**
     * 惠下单门店用户id
     */
    private Long storeCustomerId;
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
     * 取货方式（1、自提，多个用逗号分隔）
     */
    private String pickupWay;
    /**
     * 支付方式（1、微信在线付款2、微信扫码付款，多个用逗号分隔）
     */
    private String paymentWay;
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
    private String openId;
    private Date created;
    private Long createdBy;
    private String createdByName;
    private Date updated;
    private Long updatedBy;
    private String updatedByName;
    /**
     * 来源
     */
    private String source;
    /**
     * 惠小店状态（0、未开店，1、有效，2、无效）
     */
    private Byte storeStatus;
    private String token;
    private static final long serialVersionUID = 1L;

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName == null ? null : storeName.trim();
    }

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

    public String getPickupWay() {
        return pickupWay;
    }

    public void setPickupWay(String pickupWay) {
        this.pickupWay = pickupWay;
    }

    public String getPaymentWay() {
        return paymentWay;
    }

    public void setPaymentWay(String paymentWay) {
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

    public String getOpenId() {
        return openId;
    }

    public void setOpenid(String openId) {
        this.openId = openId == null ? null : openId.trim();
    }
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName == null ? null : createdByName.trim();
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedByName() {
        return updatedByName;
    }

    public void setUpdatedByName(String updatedByName) {
        this.updatedByName = updatedByName == null ? null : updatedByName.trim();
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

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
    
}