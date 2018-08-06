package com.winhxd.b2c.common.domain.system.login.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description: 用户订单信息VO
 * @author chengyy
 * @date 2018/8/6 14:29
 */
@Data
@ApiModel("用户订单信息")
public class CustomerOrderInfoVO {

    @ApiModelProperty("用户信息对象")
    private CustomerUserInfoVO1 customer;

    @ApiModelProperty("领取的优惠卷的总次数")
    private Integer couponCount;

}
