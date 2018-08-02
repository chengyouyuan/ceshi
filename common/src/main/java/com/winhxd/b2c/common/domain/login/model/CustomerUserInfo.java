package com.winhxd.b2c.common.domain.login.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wufuyun
 * @date  2018年8月2日 下午3:44:27
 * @Description c端用户信息
 * @version
 */
public class CustomerUserInfo implements Serializable {
	/**
	 * 用户主键
	 */
    private Long customerId;
    /**
     * 用户账号
     */
    private String customerMobile;
    /**
     * 微信openId
     */
    private String openId;
    /**
     * 纬度
     */
    private Double lat;
    /**
     * 经度
     */
    private Double lon;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 头像
     */
    private String headurl;
    
    private Date created;

    private Date updated;
    /**
     * 会话秘钥
     */
    private String  sessionKey;
    private static final long serialVersionUID = 1L;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile == null ? null : customerMobile.trim();
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId == null ? null : openId.trim();
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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName == null ? null : nickName.trim();
    }

    public String getHeadurl() {
        return headurl;
    }

    public void setHeadurl(String headurl) {
        this.headurl = headurl == null ? null : headurl.trim();
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

	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}
    
}