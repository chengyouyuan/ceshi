package com.winhxd.b2c.common.domain.order.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author pangjianhua
 * @date 2018/8/3 13:28
 */
@Data
public class OrderInfoSimpleVo {
    @ApiModelProperty(value = "订单编号", required = true)
    private String orderNo;
    /**
     * 接单门店ID
     */
    @ApiModelProperty(value = "接单门店ID", required = true)
    private Long storeId;
    /**
     * 计价类型:1:线上计价;2:线下计价;
     */
    @ApiModelProperty(value = "计价类型:1:线上计价;2:线下计价;", required = true)
    private Short valuationType;
    /**
     * 订单状态 1:已提交;3:待接单;5:待计价;7:已计价;
     * 9:待自提(已确认);10:待配送(已确认);11:待顾客确认;13:已完成;99:已取消;77:已退款;33:待退款;
     */
    @ApiModelProperty(value = "订单状态 1:已提交;3:待接单;5:待计价;7:已计价;9:待自提(已确认);10:待配送(已确认);11:待顾客确认;13:已完成;99:已取消;77:已退款;33:待退款;", required = true)
    private Short orderStatus;
}
