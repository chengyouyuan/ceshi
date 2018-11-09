package com.winhxd.b2c.common.domain.customer.model;

import lombok.Data;

import java.util.Date;

@Data
public class CustomerAddress {
    private Long id;

    private Long customerId;

    private String contacterName;

    private String contacterMobile;

    private String contacterProvince;

    private String contacterCity;

    private String contacterCounty;

    private String contacterDetailAddress;

    private Long labelId;

    private Boolean defaultAddress;

    private Date created;

    private Date updated;

}