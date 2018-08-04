package com.winhxd.b2c.common.domain.product.vo;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * 商品列表VO
 * @ClassName:  ProductsVO   
 * @Description:TODO   
 * @author: zhuchongchen 
 * @date:   2018年8月4日 下午1:09:15   
 *
 */
public class ProductVO {

	@ApiModelProperty(value = "商品编码")
	private String productCode;
	
	@ApiModelProperty(value = "商品名称")
	private String productName;
	
	@ApiModelProperty(value = "商品图片")
	private String productImage;
	
	@ApiModelProperty(value = "商品规格")
	private List<String> skuAttributeOption;

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductImage() {
		return productImage;
	}

	public void setProductImage(String productImage) {
		this.productImage = productImage;
	}

	public List<String> getSkuAttributeOption() {
		return skuAttributeOption;
	}

	public void setSkuAttributeOption(List<String> skuAttributeOption) {
		this.skuAttributeOption = skuAttributeOption;
	}

}
