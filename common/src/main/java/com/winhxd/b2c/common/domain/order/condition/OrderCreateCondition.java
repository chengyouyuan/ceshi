package com.winhxd.b2c.common.domain.order.condition;

import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OrderCreateCondition {

    @ApiModelProperty(value = "门店id", required=true)
    private Long storeId;
    
    @ApiModelProperty(value = "用户id", required=true)
    private Long customerId;
    
    @ApiModelProperty(value = "支付类型", required=true)
    private Short payType;
    
    @ApiModelProperty(value = "优惠券id", required=false)
    private Long[] couponIds;

    @ApiModelProperty(value = "提货类型:1:门店自提;2:送货上门", required=true)
    private Short pickupType;

    @ApiModelProperty(value = "订单收货人", required=false)
    private String orderConsignee;

    @ApiModelProperty(value = "订单收货人电话", required=false)
    private String orderConsigneeMobile;

    @ApiModelProperty(value = "订单收货地址", required=false)
    private String orderAddress;

    @ApiModelProperty(value = "自提时间", required=false)
    private Date pickupDateTime;
    
    @ApiModelProperty(value = "备注", required=false)
    private String remark;
    
    @ApiModelProperty(value = "订单商品明细", required=true)
    private List<OrderItemCondition> orderItemConditions;

}
