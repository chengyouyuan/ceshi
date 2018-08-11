package com.winhxd.b2c.common.domain.promotion.condition;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

import org.apache.commons.lang3.time.DateFormatUtils;

import com.winhxd.b2c.common.domain.common.PagedCondition;

public class CouponCheckStatusCondition extends PagedCondition {
	@ApiModelProperty(value = "用户id")
	private Long customerId;

	@ApiModelProperty(value = "门店id模板集合")
	private List<Long> templateIds;

	@ApiModelProperty(value = "发放id")
	private Long sendId;

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Long getSendId() {
		return sendId;
	}

	public void setSendId(Long sendId) {
		this.sendId = sendId;
	}
}
