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
    @ApiModelProperty(value = "订单状态 1:已提交;2:待付款；3:待接单;7:已计价; 9:待自提(已确认);22:已完成;99:已取消;77:已退款;33:待退款；66:退款中；", required = true)
    private Short orderStatus;
    @ApiModelProperty(value = "订单总金额", required = true)
    private BigDecimal orderTotalMoney;
    @ApiModelProperty(value = "订单创建时间", required = true)
    private Date created;
}
