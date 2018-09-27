package com.winhxd.b2c.common.domain.store.model;

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
     * 门店简称称
     */
    private String storeShortName;
    /**
     * 惠下单门店用户id(云平台crm_ws_customer表的customer_id)
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
    private String shopOwnerImg;
    /**
     * 取货方式（1、自提，多个用逗号分隔）
     */
    private String pickupType;
    /**
     * 支付方式（1、微信在线付款2、微信扫码付款，多个用逗号分隔）
     */
    private String payType;
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
    /**
     * 微信昵称
     */
    private String wechatName;
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
     * StoreStatusEnum
     */
    private Short storeStatus;
    private String token;
    /**
     * app登录状态:0登录、1退出;
     */
    private Short appLoginStatus;
    public String getMiniProgramCodeUrl() {
        return miniProgramCodeUrl;
    }

    public void setMiniProgramCodeUrl(String miniProgramCodeUrl) {
        this.miniProgramCodeUrl = miniProgramCodeUrl;
    }

    /**门店小程序码的地址*/

    private String miniProgramCodeUrl;

    /**门店门头照地址*/

    private String storePicImg;

    private static final long serialVersionUID = 1L;

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName == null ? null : storeName.trim();
    }

    public String getStoreShortName() {
        return storeShortName;
    }

    public void setStoreShortName(String storeShortName) {
        this.storeShortName = storeShortName == null ? null : storeShortName.trim();
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

    public String getShopOwnerImg() {
        return shopOwnerImg;
    }

    public void setShopOwnerImg(String shopOwnerImg) {
        this.shopOwnerImg = shopOwnerImg;
    }

    public String getShopkeeper() {
        return shopkeeper;
    }

    public void setShopkeeper(String shopkeeper) {
        this.shopkeeper = shopkeeper == null ? null : shopkeeper.trim();
    }

    public String getPickupType() {
        return pickupType;
    }

    public void setPickupType(String pickupType) {
        this.pickupType = pickupType;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
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

    public String getWechatName() {
        return wechatName;
    }

    public void setWechatName(String wechatName) {
        this.wechatName = wechatName == null ? null : wechatName.trim();
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

    public Short getStoreStatus() {
        return storeStatus;
    }

    public void setStoreStatus(Short storeStatus) {
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

	public Short getAppLoginStatus() {
		return appLoginStatus;
	}

	public void setAppLoginStatus(Short appLoginStatus) {
		this.appLoginStatus = appLoginStatus;
	}

    public String getStorePicImg() {
        return storePicImg;
    }

    public void setStorePicImg(String storePicImg) {
        this.storePicImg = storePicImg;
    }
}