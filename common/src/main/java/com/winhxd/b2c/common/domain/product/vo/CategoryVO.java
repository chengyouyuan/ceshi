package com.winhxd.b2c.common.domain.product.vo;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;

public class CategoryVO {

	@ApiModelProperty(value = "分类编码")
	private String categoryCode;
	
	@ApiModelProperty(value = "分类名称")
	private String categoryName;
	
	@ApiModelProperty(value = "分类子集")
	private List<CategoryVO> categorys;

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public List<CategoryVO> getCategorys() {
		return categorys;
	}

	public void setCategorys(List<CategoryVO> categorys) {
		this.categorys = categorys;
	}
	
	
}
