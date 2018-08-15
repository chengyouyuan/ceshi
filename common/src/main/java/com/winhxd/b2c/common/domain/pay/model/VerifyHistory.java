package com.winhxd.b2c.common.domain.pay.model;

import lombok.Data;

import java.util.Date;

@Data
public class VerifyHistory {

    private Long id;

    private Integer verifyStatus;

    private String verifyCode;

    private String verifyRemark;

    private Date operatedTime;

    private Long operatedBy;

    private String operatedByName;
}
