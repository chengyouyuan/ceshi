package com.winhxd.b2c.common.domain.order.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单
 *
 * @author pangjianhua
 * @date 2018/8/2 14:50
 */
@Data
public class OrderInfo {

    private Long id;

    private String orderNo;

    private Long customerId;

    private Long storeId;

    private Short valuationType;

    private Short orderStatus;

    private String pickupCode;

    private BigDecimal randomReduction;

    private BigDecimal couponHxdMoney;

    private BigDecimal couponBrandMoney;

    private BigDecimal orderTotalMoney;

    private Long realPaymentMoney;

    private Short payType;

    private Short payStatus;

    private Date created;

    private Date payFinishDatetime;

    private Date pickupDatetime;

    private Date finishDatetime;

    private Date cancelDatetime;

    private Short pickupType;

    private String inviterCode;

    private Date updated;

    private String cancelReason;

    private String remark;

    private String imie;

    private Long createdBy;

    private Long updatedBy;

    private String updatedByName;

    private String createdByName;
}