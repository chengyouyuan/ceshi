package com.winhxd.b2c.common.domain.promotion.condition;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 * @author shijinxing
 * @date 2018/8/6
 */
@Data
@ApiModel(value = "用户请求参数",description = "后台用户列表请求参数")
public class CouponActivityCondition {

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
