package com.winhxd.b2c.common.domain.product.condition;

import io.swagger.annotations.ApiModelProperty;

/**
 * 查询商品信息 condition
 * @ClassName:  ProductCondition   
 * @Description:TODO   
 * @author: zhuchongchen 
 * @date:   2018年8月4日 下午1:06:11   
 *
 */
public class BrandCondition {
	
	@ApiModelProperty(value = "品牌商编码")
	private String companyCode;
	
    @ApiModelProperty(value = "品牌编码")
    private String brandCode;
    
    @ApiModelProperty(value = "品牌名称")
    private String brandName;

    @ApiModelProperty(value = "每页显示条数" ,required = true)
    private Integer pageSize;
    
	@ApiModelProperty(value = "页号" ,required = true)
    private Integer pageNo;
    
	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
    
	
}
