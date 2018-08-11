package com.winhxd.b2c.common.domain.order.condition;

import com.winhxd.b2c.common.domain.common.PagedCondition;

import io.swagger.annotations.ApiModelProperty;

/**
 * 门店、用户订单列表查询
 * @author wangbin
 * @date  2018年8月6日 下午2:58:44
 * @version 
 */
public class OrderInfoQuery4ManagementCondition extends PagedCondition {

    @ApiModelProperty(value = "门店id")
    private Long storeId;
    
    @ApiModelProperty(value = "用户id")
    private Long customerId;

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
    
}
