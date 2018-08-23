package com.winhxd.b2c.common.domain.order.condition;

import com.winhxd.b2c.common.domain.common.ApiCondition;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by wangbaokuo on 2018/8/23 15:04
 */
@ApiModel("获取订单实付金额，优惠金额入参")
@Data
public class OrderMoneyCondition extends ApiCondition {
    @ApiModelProperty(value = "门店ID", required = true)
    private Long storeId;
    @ApiModelProperty(value = "门店ID", required = true)
    private Long sendId;
}
