package com.winhxd.b2c.common.domain.pay.vo;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class PayOrderPaymentVO{
	
    private Long id;

    private String orderNo;

    private String orderPamentNo;

    private Date created;

    private Date updated;

    private Date callbackDate;

    private String timeEnd;

    private Short callbackStatus;

    private String callbackReason;

    private String buyerId;

    private String transactionId;

    private BigDecimal realPaymentMoney;

    private BigDecimal callbackMoney;

    private Short payType;

    private BigDecimal cmmsAmt;

    private BigDecimal rate;
}