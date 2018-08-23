package com.winhxd.b2c.common.domain.pay.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by wangbaokuo on 2018/8/23 13:56
 */
@ApiModel("计算订单金额返回数据")
@Data
public class OrderMoneyVO {
    @ApiModelProperty("合计金额")
    private BigDecimal orderTotalMoney = BigDecimal.ZERO;
    @ApiModelProperty(value = "折扣总额")
    private BigDecimal discountAmount = BigDecimal.ZERO;
}
