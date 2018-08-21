package com.winhxd.b2c.common.domain.pay.vo;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("提现确认参数")
@Data
public class PayStoreApplyWithdrawVO {
	@ApiModelProperty("实际到账金额")
    private BigDecimal realFee;
    @ApiModelProperty("手续费")
    private BigDecimal cmmsAmt;
    @ApiModelProperty("feilv")
    private BigDecimal rate;
}
