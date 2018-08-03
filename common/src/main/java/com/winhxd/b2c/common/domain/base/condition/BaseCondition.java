package com.winhxd.b2c.common.domain.base.condition;

import lombok.Data;

@Data
public abstract class BaseCondition {

	/**
	 * 页号
	 */
	protected int pageNo = 1;

	/**
	 * 每页显示行数
	 */
	protected int pageSize = 10;

}
