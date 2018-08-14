package com.winhxd.b2c.common.domain.promotion.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author sjx
 */
@Data
@ApiModel(value = "活动优惠券门店用户",description = "活动优惠券门店用户")
public class CouponActivityStoreCustomer {
    private Long id;

    @ApiModelProperty(value = "优惠券活动模板关联id")
    private Long couponActivityTemplateId;

    @ApiModelProperty(value = "门店id")
    private Long storeId;

    @ApiModelProperty(value = "用户id")
    private Long customerId;

    @ApiModelProperty(value = "是否有效0无效1有效")
    private Short status;
}