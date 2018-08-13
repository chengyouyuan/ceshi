package com.winhxd.b2c.common.domain.order.condition;

import com.winhxd.b2c.common.domain.common.ApiCondition;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by wangbaokuo on 2018/8/13 20:35
 */
@ApiModel("查询购物车")
@Data
public class ShopCarQueryCondition extends ApiCondition {
    @ApiModelProperty(value = "门店ID")
    private Long storeId;
}
