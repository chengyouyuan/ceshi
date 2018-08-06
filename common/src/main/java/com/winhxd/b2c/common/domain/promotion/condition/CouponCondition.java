package com.winhxd.b2c.common.domain.promotion.condition;

import com.winhxd.b2c.common.domain.base.condition.BaseCondition;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Auther wangxiaoshun
 * @Date 2018/8/6 10 56
 * @Description
 */
public class CouponCondition  extends BaseCondition {
    @ApiModelProperty(value = "用户id", required=true)
    private Long customerId;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}
