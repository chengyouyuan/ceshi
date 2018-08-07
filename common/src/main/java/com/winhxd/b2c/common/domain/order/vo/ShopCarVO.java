package com.winhxd.b2c.common.domain.order.vo;

import com.winhxd.b2c.common.domain.order.condition.OrderItemCondition;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author: wangbaokuo
 * @date: 2018/8/3 10:12
 * @description:
 */
@ApiModel("购物车")
@Data
public class ShopCarVO {

    @ApiModelProperty(value = "购物车ID")
    private Long id;

    @ApiModelProperty(value = "用户ID")
    private Long customerId;

    @ApiModelProperty(value = "门店ID")
    private Long storeId;

    @ApiModelProperty(value = "订单商品明细")
    private List<ShopCarProdInfoVO> shopCarProdInfoVOs;

    @ApiModelProperty(value = "创建时间")
    private Date created;

    @ApiModelProperty(value = "修改时间")
    private Date updated;

    @ApiModelProperty(value = "创建人")
    private Long createdby;

    @ApiModelProperty(value = "更新人")
    private Long updatedby;

}
