package com.winhxd.b2c.common.domain.base.condition;

public abstract class BaseCondition {

	/**
	 * 页号
	 */
	protected int pageNo = 1;

	/**
	 * 每页显示行数
	 */
	protected int pageSize = 10;

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
}
