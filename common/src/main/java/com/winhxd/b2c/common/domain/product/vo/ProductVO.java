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
	
	@ApiModelProperty(value = "商品sku")
	private List<ProductSkuVO> productSkus;

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

	public List<ProductSkuVO> getProductSkus() {
		return productSkus;
	}

	public void setProductSkus(List<ProductSkuVO> productSkus) {
		this.productSkus = productSkus;
	}



}
