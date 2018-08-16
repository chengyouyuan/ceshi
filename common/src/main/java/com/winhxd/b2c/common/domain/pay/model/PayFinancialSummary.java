package com.winhxd.b2c.common.domain.pay.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 *
 * @author sjx
 * @date 2018/8/16
 */
@Data
public class PayFinancialSummary {

    @ApiModelProperty("提现金额")
    private BigDecimal realFee;

    @ApiModelProperty("提现手续费")
    private BigDecimal cmmsAmt;

    @ApiModelProperty("退款金额")
    private BigDecimal refundAmout;
}
