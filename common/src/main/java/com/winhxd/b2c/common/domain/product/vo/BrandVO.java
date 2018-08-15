package com.winhxd.b2c.common.domain.product.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BrandVO {
	
	@ApiModelProperty(value = "品牌商编码")
	private String companyCode;
	
	@ApiModelProperty(value = "品牌商名称")
	private String companyName;
	
	@ApiModelProperty(value = "品牌编码")
	private String brandCode;
	
	@ApiModelProperty(value = "品牌名称")
	private String brandName;
	
	@ApiModelProperty(value = "品牌图片")
	private String brandImg;
}
