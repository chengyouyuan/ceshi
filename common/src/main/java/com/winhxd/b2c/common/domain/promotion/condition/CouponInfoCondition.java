package com.winhxd.b2c.common.domain.promotion.condition;

import com.winhxd.b2c.common.domain.common.ApiCondition;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Auther wangxiaoshun
 * @Date 2018/8/6 10 56
 * @Description
 */
public class CouponInfoCondition  extends ApiCondition {
    @ApiModelProperty(value = "用户id")
    private Long customerId;

    @ApiModelProperty(value = "门店id")
    private Long storeId;
    
    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}
}
