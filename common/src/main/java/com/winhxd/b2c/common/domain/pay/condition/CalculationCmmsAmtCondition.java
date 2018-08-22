package com.winhxd.b2c.common.domain.pay.condition;

import java.math.BigDecimal;

import com.winhxd.b2c.common.domain.common.ApiCondition;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("计算提现手续费对象")
@Data
public class CalculationCmmsAmtCondition extends ApiCondition {
	
	@ApiModelProperty("提现类型 1微信2银行卡")
    private Short withdrawType;
	
    @ApiModelProperty("提现金额")
    private BigDecimal totalFee;
	
}
