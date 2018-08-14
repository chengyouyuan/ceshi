package com.winhxd.b2c.common.domain.pay.condition;

import com.winhxd.b2c.common.domain.common.ApiCondition;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Author wl
 * @Date 2018/8/14 17:11
 * @Description  资金提现查询条件
 **/
public class PayStoreCashCondition extends ApiCondition {
    @ApiModelProperty("门店id")
    private Long storeId;

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }
}
