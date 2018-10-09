package com.winhxd.b2c.common.domain.promotion.condition;

import com.winhxd.b2c.common.domain.common.ApiCondition;
import io.swagger.annotations.ApiModelProperty;

public class CouponPushCondition  extends ApiCondition {

    @ApiModelProperty(value = "处理绑定状态(0:未绑定门店 1:初次绑定该门店，2.已经绑定过该门店，3.已绑过其它门店)")
    private Short bindingStatus;

    public Short getBindingStatus() {
        return bindingStatus;
    }

    public void setBindingStatus(Short bindingStatus) {
        this.bindingStatus = bindingStatus;
    }
}
