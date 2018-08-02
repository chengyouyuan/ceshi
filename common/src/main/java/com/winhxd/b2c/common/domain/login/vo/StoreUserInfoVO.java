package com.winhxd.b2c.common.domain.login.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * @author wufuyun
 * @date  2018年8月2日 下午4:38:19
 * @Description 
 * @version
 */
@ApiModel("惠小店登录返参")
@Data
public class StoreUserInfoVO implements Serializable {
	@ApiModelProperty(value = "用户id")
    private Long businessId;
	@ApiModelProperty(value = "门店名称")
    private String storeName;
	@ApiModelProperty(value = "门店编码")
    private Long storeId;
	@ApiModelProperty(value = "账号")
    private String storeMobile;
	@ApiModelProperty(value = "地址")
    private String storeAddress;
	@ApiModelProperty(value = "店主名称")
    private String shopkeeper;
	@ApiModelProperty(value = "店主头像")
    private String shopOwnerUrl;

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
		this.storeName = storeName;
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
		this.storeMobile = storeMobile;
	}


	public String getStoreAddress() {
		return storeAddress;
	}


	public void setStoreAddress(String storeAddress) {
		this.storeAddress = storeAddress;
	}


	public String getShopkeeper() {
		return shopkeeper;
	}


	public void setShopkeeper(String shopkeeper) {
		this.shopkeeper = shopkeeper;
	}


	public String getShopOwnerUrl() {
		return shopOwnerUrl;
	}


	public void setShopOwnerUrl(String shopOwnerUrl) {
		this.shopOwnerUrl = shopOwnerUrl;
	}

}