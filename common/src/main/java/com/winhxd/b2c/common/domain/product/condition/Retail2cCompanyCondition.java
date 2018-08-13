package com.winhxd.b2c.common.domain.product.condition;

import io.swagger.annotations.ApiModelProperty;

/**
 * 查询品牌信息 condition
 * @ClassName:  ProductCondition   
 * @Description:TODO   
 * @author: zhuchongchen 
 * @date:   2018年8月4日 下午1:06:11   
 *
 */
public class Retail2cCompanyCondition{
	
	@ApiModelProperty(value = "品牌商编码")
	private String companyCode;
	
    @ApiModelProperty(value = "品牌商名称")
    private String companyName;

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

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
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
