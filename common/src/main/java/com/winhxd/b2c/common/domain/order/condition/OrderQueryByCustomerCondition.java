package com.winhxd.b2c.common.domain.order.condition;

import com.winhxd.b2c.common.domain.common.ApiCondition;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author pangjianhua
 * @date 2018/8/3 17:12
 */
@Data
public class OrderQueryByCustomerCondition extends ApiCondition {
    @ApiModelProperty(value = "订单编号，查询订单详情时需传递编号")
    private String orderNo;
}
