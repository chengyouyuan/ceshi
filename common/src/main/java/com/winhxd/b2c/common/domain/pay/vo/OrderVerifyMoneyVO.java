package com.winhxd.b2c.common.domain.pay.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderVerifyMoneyVO {

    private String orderNo;

    private Long storeId;

    private BigDecimal paymentMoney;

    private BigDecimal discountMoney;
}
