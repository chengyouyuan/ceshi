package com.winhxd.b2c.common.domain.pay.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class PayWithdrawalsType implements Serializable {

    private Long id;

    private Short type;

    private String remarks;

    private Short status;

    private static final long serialVersionUID = 1L;


}