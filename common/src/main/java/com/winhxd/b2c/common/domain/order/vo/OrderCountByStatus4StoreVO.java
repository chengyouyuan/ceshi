package com.winhxd.b2c.common.domain.order.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OrderCountByStatus4StoreVO {

    @ApiModelProperty(value = "全部订单数量")
    private Integer allNum = 0;
    
    @ApiModelProperty(value = "待接单数量")
    private Integer unreceivedNum = 0;
    
    @ApiModelProperty(value = "待自提数量")
    private Integer waitSelfLiftingNum = 0;

    @ApiModelProperty(value = "待送货数量")
    private Integer waitDeliveryNum = 0;
    
    @ApiModelProperty(value = "待退款数量")
    private Integer waitRefundNum = 0;
    
    @ApiModelProperty(value = "待付款数量")
    private Integer waitPayNum = 0;
    
    @ApiModelProperty(value = "已完成数量")
    private Integer finishedNum = 0;
    
    @ApiModelProperty(value = "已退款数量")
    private Integer refundedNum = 0;
    
    @ApiModelProperty(value = "退款中数量")
    private Integer refundingNum = 0;
    
    @ApiModelProperty(value = "已取消数量")
    private Integer canceledNum = 0;

}
