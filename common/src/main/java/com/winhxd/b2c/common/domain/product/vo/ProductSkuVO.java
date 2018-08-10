package com.winhxd.b2c.common.domain.product.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ProductSkuVO {

	@ApiModelProperty(value = "品牌编码")
	private String brandCode;
	
	@ApiModelProperty(value = "品牌名称")
	private String brandName;
	
	@ApiModelProperty(value = "商品编码")
	private String productCode;
	
	@ApiModelProperty(value = "sku编码")
	private String skuCode;
	
	@ApiModelProperty(value = "商品名称")
	private String prodName;
	
	@ApiModelProperty(value = "商品图片")
	private String skuImage;
	
	@ApiModelProperty(value = "商品规格")
	private String skuAttributeOption;
	
}
