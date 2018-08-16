package com.winhxd.b2c.common.domain.promotion.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 * @author sjx
 * @date 2018/8/16
 */
@Data
public class CouponKindsVo {
    @ApiModelProperty(value = "用户可领取门店优惠券种类数")
    private Integer storeCouponKinds;
}
