package com.winhxd.b2c.common.domain.product.condition;

import java.util.List;


import io.swagger.annotations.ApiModelProperty;

/**
 * 查询商品信息 condition
 * @ClassName:  ProductCondition   
 * @Description:TODO   
 * @author: zhuchongchen 
 * @date:   2018年8月4日 下午1:06:11   
 *
 */
public class ProductCondition {
	
    @ApiModelProperty(value = "门店在惠下单购买过商品sku")
    private List<String> hxdProductSkus;
    
    @ApiModelProperty(value = "门店已上架商品sku", required = true)
    private List<String> productSkus;
    
    @ApiModelProperty(value = "商品标准款sku")
    private List<String> brandCodes;
    
    @ApiModelProperty(value = "一级品类编码")
    private String categoryCode;
    
    @ApiModelProperty(value = "二级品类编码")
    private List<String> categoryCodes;
    
    @ApiModelProperty(value = "商品名称")
    private String productName;

	public List<String> getHxdProductSkus() {
		return hxdProductSkus;
	}

	public void setHxdProductSkus(List<String> hxdProductSkus) {
		this.hxdProductSkus = hxdProductSkus;
	}

	public List<String> getProductSkus() {
		return productSkus;
	}

	public void setProductSkus(List<String> productSkus) {
		this.productSkus = productSkus;
	}

	public List<String> getBrandCodes() {
		return brandCodes;
	}

	public void setBrandCodes(List<String> brandCodes) {
		this.brandCodes = brandCodes;
	}

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public List<String> getCategoryCodes() {
		return categoryCodes;
	}

	public void setCategoryCodes(List<String> categoryCodes) {
		this.categoryCodes = categoryCodes;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}
    
    
}
