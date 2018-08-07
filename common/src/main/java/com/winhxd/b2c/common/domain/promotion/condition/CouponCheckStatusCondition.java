package com.winhxd.b2c.common.domain.promotion.condition;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;

public class CouponCheckStatusCondition extends CommonCondition {
	@ApiModelProperty(value = "用户id")
	private Long customerId;

	@ApiModelProperty(value = "门店id模板集合")
	private List<Long> templateIds;

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

}
