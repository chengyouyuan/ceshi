package com.winhxd.b2c.common.domain.product.vo;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;

public class ProductSkuVO {

	@ApiModelProperty(value = "sku编码")
	private String skuCode;
	
	@ApiModelProperty(value = "商品名称")
	private String skuName;
	
	@ApiModelProperty(value = "商品图片")
	private String skuImage;
	
	@ApiModelProperty(value = "商品规格")
	private String skuAttributeOption;

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public String getSkuName() {
		return skuName;
	}

	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}

	public String getSkuImage() {
		return skuImage;
	}

	public void setSkuImage(String skuImage) {
		this.skuImage = skuImage;
	}

	public String getSkuAttributeOption() {
		return skuAttributeOption;
	}

	public void setSkuAttributeOption(String skuAttributeOption) {
		this.skuAttributeOption = skuAttributeOption;
	}
	
}
