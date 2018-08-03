package com.winhxd.b2c.common.domain.order.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author pangjianhua
 * @date 2018/8/3 10:07
 */
@Data
public class OrderListVO {
    @ApiModelProperty(value = "订单商品详情", required = true)
    private List<OrderItemVO> orderItemVoList;
    @ApiModelProperty(value = "订单总金额 未计价订单有可能未空")
    private BigDecimal orderTotalMoney;
    @ApiModelProperty(value = "支付类型:1为微信扫码付款;2为微信在线支付;", required = true)
    private Short payType;
    @ApiModelProperty(value = "支付状态:0为未支付;1为已支付;", required = true)
    private Short payStatus;
    @ApiModelProperty(value = "计价类型:1:线上计价;2:线下计价;", required = true)
    private Short valuationType;
    @ApiModelProperty(value = "订单状态 1:已提交;3:待接单;5:待计价;7:已计价;9:待自提(已确认);11:待顾客确认;13:已完成;99:已取消;77:已退款;33:待退款;", required = true)
    private Short orderStatus;
    @ApiModelProperty(value = "订单创建时间", required = true)
    private Date created;
}
