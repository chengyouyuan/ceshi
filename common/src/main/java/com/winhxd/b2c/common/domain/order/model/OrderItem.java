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
public class OrderItem {
    private Long id;
    /**
     * 订单编号
     */
    private String orderNo;
    /**
     * 商品SKU
     */
    private String skuCode;
    /**
     * 商品SKU 信息描述
     */
    private String skuDesc;
    /**
     * 商品SKU 图片
     */
    private String skuUrl;
    /**
     * 商品数量
     */
    private Integer amount;
    /**
     * 商品价格
     */
    private BigDecimal price;

    private Date created;

    private Long createdBy;

    private Date updated;

    private Long updatedBy;

    private String updatedByName;

    private String createdByName;

    private String barCode;

}