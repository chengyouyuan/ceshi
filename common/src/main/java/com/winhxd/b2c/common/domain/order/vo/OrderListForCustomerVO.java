package com.winhxd.b2c.common.domain.order.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author pangjianhua
 * @date 2018/8/13 16:28
 */
@Data
public class OrderListForCustomerVO {
    @ApiModelProperty(value = "订单商品详情", required = true)
    private List<OrderListItemForCustomerVO> orderItemVoList;
    @ApiModelProperty(value = "订单编号", required = true)
    private String orderNo;
    @ApiModelProperty(value = "订单状态描述", required = true)
    private String orderStatusDesc;
    @ApiModelProperty(value = "订单总金额", required = true)
    private BigDecimal orderTotalMoney;
    @ApiModelProperty(value = "订单创建时间", required = true)
    private Date created;
}
