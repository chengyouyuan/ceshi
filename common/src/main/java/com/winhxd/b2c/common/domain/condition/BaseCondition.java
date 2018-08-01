package com.winhxd.b2c.common.domain.condition;

import com.winhxd.b2c.common.domain.base.BaseDomain;
import lombok.Data;

@Data
public abstract class BaseCondition extends BaseDomain {

	/**
	 * 页号
	 */
	protected int pageNo = 1;

	/**
	 * 每页显示行数
	 */
	protected int pageSize = 10;

}
