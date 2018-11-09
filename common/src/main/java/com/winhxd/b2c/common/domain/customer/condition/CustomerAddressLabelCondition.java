package com.winhxd.b2c.common.domain.customer.condition;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author chenyanqi
 * 用户地址标签入参
 */
@ApiModel("用户地址标签入参")
@Data
public class CustomerAddressLabelCondition {

    /**
     * 标签id
     */
    @ApiModelProperty("标签id")
    private Long id;
    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Long customerId;

    /**
     * 用户地址标签名字
     */
    @ApiModelProperty("用户地址标签名字")
    private String labelName;

}
