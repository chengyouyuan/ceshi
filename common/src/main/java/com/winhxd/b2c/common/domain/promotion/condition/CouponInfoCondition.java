<<<<<<< HEAD
package com.winhxd.b2c.common.domain.promotion.condition;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Auther wangxiaoshun
 * @Date 2018/8/6 10 56
 * @Description
 */
public class CouponInfoCondition  extends CommonCondition {
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
=======
package com.winhxd.b2c.common.domain.promotion.condition;

import com.winhxd.b2c.common.domain.common.PagedCondition;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Auther wangxiaoshun
 * @Date 2018/8/6 10 56
 * @Description
 */
public class CouponInfoCondition  extends PagedCondition {
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
>>>>>>> branch 'master' of git@192.168.1.101:retail2c/retail2c-backend.git
