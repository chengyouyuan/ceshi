package com.winhxd.b2c.common.domain.pay.vo;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CalculationCmmsAmtVO {
	@ApiModelProperty("实际到账金额")
    private BigDecimal realFee;
    @ApiModelProperty("手续费")
    private BigDecimal cmmsAmt;
}
