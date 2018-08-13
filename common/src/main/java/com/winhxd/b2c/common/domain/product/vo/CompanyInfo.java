package com.winhxd.b2c.common.domain.product.vo;

import io.swagger.annotations.ApiModelProperty;

public class CompanyInfo {
	
	@ApiModelProperty(value = "品牌商编码")
	private String companyCode;
	
	@ApiModelProperty(value = "品牌商名称")
	private String companyName;

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
}
