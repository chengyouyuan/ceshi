package com.winhxd.b2c.common.domain.product.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductSkuVO {

	@ApiModelProperty(value = "品牌商编码")
	private String companyCode;
	
	@ApiModelProperty(value = "品牌编码")
	private String brandCode;
	
	@ApiModelProperty(value = "品牌名称")
	private String brandName;
	
	@ApiModelProperty(value = "商品编码")
	private String productCode;
	
	@ApiModelProperty(value = "sku编码")
	private String skuCode;
	
	@ApiModelProperty(value = "条形码")
	private String barCode;
	
	@ApiModelProperty(value = "商品名称")
	private String skuName;
	
	@ApiModelProperty(value = "商品图片")
	private String skuImage;
	
	@ApiModelProperty(value = "商品规格")
	private String skuAttributeOption;

	@ApiModelProperty(value = "商品价格")
	private BigDecimal sellMoney;

	@ApiModelProperty(value = "购物车数量")
	private Integer amount;
	
	@ApiModelProperty(value = "一级分类")
	private String categoryId;
	
}
