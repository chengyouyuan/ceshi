package com.winhxd.b2c.common.domain.message.model;

import lombok.Data;

import java.util.Date;

/**
 * @author jujinbiao
 */
@Data
public class MessageCustomerFormIds {
    private Long id;

    private String openid;

    private String formid;

    private Date created;
}