package com.winhxd.b2c.common.domain.order.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单详情表
 *
 * @author pangjianhua
 * @date 2018/8/2 14:50
 */
@Data
public class OrderDetail {
    private Long id;

    private String orderNo;

    private String sku;

    private Integer amount;

    private BigDecimal price;

    private Date created;

    private Long createdBy;

    private Date updated;

    private Long updatedBy;

    private String updatedByName;

    private String createdByName;
}