package com.winhxd.b2c.common.domain.customer.model;

import lombok.Data;

import java.util.Date;

@Data
public class CustomerAddressLabel {
    private Long id;

    private Long customerId;

    private Short labelType;

    private String labelName;

    private Date created;

}