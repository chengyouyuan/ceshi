package com.winhxd.b2c.common.domain.customer.model;

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
    private String openid;
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
    private String headImg;
    
    private Date created;
    private Long createdBy;
    private String createdByName;
    private Date updated;
    private Long updatedBy;
    private String updatedByName;
    private String token;
    /**用户状态,默认有效1，无效(黑名单)0,默认是有效*/
    private Integer status = 1;
    /**
     * 会话秘钥
     */
    private String sessionKey;
    private static final long serialVersionUID = 1L;

    /**
     * 数盟id
     *
     * @return
     */
    private String digitalUnionId;

    public String getDigitalUnionId() {
        return digitalUnionId;
    }

    public void setDigitalUnionId(String digitalUnionId) {
        this.digitalUnionId = digitalUnionId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

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

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid == null ? null : openid.trim();
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
    public String getHeadImg() {
		return headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}

	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token == null ? null : token.trim();
	}
    
}