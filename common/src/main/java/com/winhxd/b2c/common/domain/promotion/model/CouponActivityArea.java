package com.winhxd.b2c.common.domain.promotion.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author sjx
 * @date 2018-8-17
 */
@ApiModel("优惠券活动区域信息")
@Data
public class CouponActivityArea {
    private Long id;

    @ApiModelProperty(value = "优惠券活动ID")
    private Long couponActivityId;

    @ApiModelProperty(value = "区域编码")
    private String regionCode;

    @ApiModelProperty(value = "区域")
    private String regionName;

    @ApiModelProperty(value = "是否有效 0无效1有效")
    private Short status;

}