package com.winhxd.b2c.common.domain.promotion.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 * @author shijinxing
 * @date 2018/8/6
 */
@ApiModel("优惠券活动")
@Data
public class CouponActivityVO {

    @ApiModelProperty(value = "优惠券活动ID")
    private Long id;

    @ApiModelProperty(value = "发券名称")
    private String name;

    @ApiModelProperty(value = "发券编码")
    private String code;

    @ApiModelProperty(value = "发券说明")
    private String exolian;

    @ApiModelProperty(value = "发券备注")
    private Short remarks;
}
