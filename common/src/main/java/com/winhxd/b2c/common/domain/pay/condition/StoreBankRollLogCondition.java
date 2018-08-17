package com.winhxd.b2c.common.domain.pay.condition;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Auther wangxiaoshun
 * @Date 2018/8/15 20 25
 * @Description
 */
@Data
public class StoreBankRollLogCondition {

    @ApiModelProperty("门店id")
    private Long storeId;
    @ApiModelProperty("订单金额")
    private BigDecimal orderMoeny;
    @ApiModelProperty("提现金额")
    private BigDecimal presentedMoney;
    @ApiModelProperty("结算金额")
    private BigDecimal settlementMoney;
    @ApiModelProperty("类型：1.支付完成,2.结算审核,3.提现申请,4.提现审核")
    private Integer type;
    @ApiModelProperty("订单号")
    private String orderNo;
    @ApiModelProperty("提现单号")
    private String withdrawalsNo;
}
