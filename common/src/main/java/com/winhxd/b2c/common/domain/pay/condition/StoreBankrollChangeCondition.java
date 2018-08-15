package com.winhxd.b2c.common.domain.pay.condition;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class StoreBankrollChangeCondition {
	private Long storeId;

    private BigDecimal totalMoeny;

    private BigDecimal presentedMoney;

    private BigDecimal presentedFrozenMoney;

    private BigDecimal settlementSettledMoney;
    
    private String remarks;
}
