package com.winhxd.b2c.common.domain.product.vo;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CategoryVO {

	@ApiModelProperty(value = "分类编码")
	private String categoryCode;
	
	@ApiModelProperty(value = "分类名称")
	private String categoryName;
	
	@ApiModelProperty(value = "分类子集")
	private List<CategoryVO> categorys;

	@ApiModelProperty(value = "品牌")
	private List<BrandVO> brands;
}
