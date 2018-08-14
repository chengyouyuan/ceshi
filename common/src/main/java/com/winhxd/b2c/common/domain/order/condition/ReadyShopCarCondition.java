package com.winhxd.b2c.common.domain.order.condition;

import com.winhxd.b2c.common.domain.common.ApiCondition;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author: wangbaokuo
 * @date: 2018/8/3 09:29
 * @description:
 */
@ApiModel("预订单传参")
@Data
public class ReadyShopCarCondition extends ApiCondition {

    @ApiModelProperty(value = "门店ID")
    private Long storeId;

    @ApiModelProperty(value = "自提地址")
    private String extractAddress;

    @ApiModelProperty(value = "自提时间")
    private Date pickupDateTime;

    @ApiModelProperty(value = "订单商品明细", required=true)
    private List<OrderItemCondition> orderItemConditions;

    @ApiModelProperty(value = "支付方式(1:微信扫码付款,2微信在线付款)")
    private Short payType;

    @ApiModelProperty(value = "优惠券ID")
    private Long[] couponIds;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "金额")
    private BigDecimal orderTotalMoney;

}
