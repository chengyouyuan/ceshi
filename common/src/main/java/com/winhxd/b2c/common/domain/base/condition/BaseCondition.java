package com.winhxd.b2c.common.domain.base.condition;

import io.swagger.annotations.ApiModelProperty;

public abstract class BaseCondition {

	@ApiModelProperty("页号")
	protected int pageNo = 1;

	@ApiModelProperty("每页条数")
	protected int pageSize = 10;

	@ApiModelProperty("排序字段和排序方式(例：name asc,age desc)")
	private String orderBy;

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
}
