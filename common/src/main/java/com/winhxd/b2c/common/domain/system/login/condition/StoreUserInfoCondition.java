package com.winhxd.b2c.common.domain.system.login.condition;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * @author wufuyun
 * @date  2018年8月2日 下午4:38:38
 * @Description 
 * @version
 */
@ApiModel("惠小店用户请求参数")
@Data
public class StoreUserInfoCondition implements Serializable {
	@ApiModelProperty(value = "用户账号")
    private String storeMobile;
	@ApiModelProperty(value = "密码")
    private String storePassword;
	@ApiModelProperty(value = "头像")
    private String shopOwnerUrl;
	@ApiModelProperty(value = "纬度")
    private Double lat;
	@ApiModelProperty(value = "经度")
    private Double lon;
	@ApiModelProperty(value = "微信openid")
    private String openid;
	@ApiModelProperty(value = "来源")
    private String source;

    private static final long serialVersionUID = 1L;

	public String getStoreMobile() {
		return storeMobile;
	}

	public void setStoreMobile(String storeMobile) {
		this.storeMobile = storeMobile;
	}

	public String getStorePassword() {
		return storePassword;
	}

	public void setStorePassword(String storePassword) {
		this.storePassword = storePassword;
	}

	public String getShopOwnerUrl() {
		return shopOwnerUrl;
	}

	public void setShopOwnerUrl(String shopOwnerUrl) {
		this.shopOwnerUrl = shopOwnerUrl;
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
		this.openid = openid;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

}