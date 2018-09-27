package com.winhxd.b2c.common.domain.promotion.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author  sunwenwu
 * 2018年9月27日
 */
@Data
@ApiModel(value = "推券活动所指定的用户",description = "推券活动所指定的用户")
public class CouponPushCustomer {
    private Long id;

    @ApiModelProperty(value = "推券活动所指定的用户的ID")
    private Long customerId;

    @ApiModelProperty(value = "活动ID")
    private Long couponActivityId;

    @ApiModelProperty(value = "是否领取 0:未领取1已领取")
    private Boolean receive;

    @ApiModelProperty(value = "扩展字段")
    private String temp;
}