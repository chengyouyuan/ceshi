package com.winhxd.b2c.common.domain.order.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author pangjianhua
 * @date 2018/8/3 9:52
 */
@Data
public class OrderInfoDetailVO implements Serializable {
    private static final long serialVersionUID = -2960449858467596227L;
    @ApiModelProperty(value = "订单商品详情", required = true)
    private List<OrderItemVO> orderItemVoList;
    @ApiModelProperty(value = "主键", required = true)
    private Long id;
    @ApiModelProperty(value = "订单编号", required = true)
    private String orderNo;
    /**
     * 下单用户ID
     */
    @ApiModelProperty(value = "下单用户ID", required = true)
    private Long customerId;
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
     * 9:待自提(已确认);11:待顾客确认;13:已完成;99:已取消;77:已退款;33:待退款;
     */
    @ApiModelProperty(value = "订单状态 1:已提交;3:待接单;7:已计价;9:待自提(已确认);22:已完成;99:已取消;77:已退款;33:待退款;", required = true)
    private Short orderStatus;
    /**
     * 提货码
     */
    @ApiModelProperty(value = "提货码", required = true)
    private String pickupCode;
    /**
     * 订单总金额
     */
    @ApiModelProperty(value = "订单总金额", required = true)
    private BigDecimal orderTotalMoney;
    /**
     * 优惠金额
     */
    @ApiModelProperty(value = "优惠金额", required = true)
    private BigDecimal discountMoney;
    /**
     * 订单实付金额
     */
    @ApiModelProperty(value = "订单实付金额", required = true)
    private BigDecimal realPaymentMoney;
    /**
     * 支付类型:1为微信扫码付款;2为微信在线支付;
     */
    @ApiModelProperty(value = "支付类型:1为微信扫码付款;2为微信在线支付;", required = true)
    private Short payType;
    /**
     * 支付状态:0为未支付;1为已支付;
     */
    @ApiModelProperty(value = "支付状态:0为未支付;1为已支付;", required = true)
    private Short payStatus;
    /**
     * 订单创建时间
     */
    @ApiModelProperty(value = "订单创建时间", required = true)
    private Date created;
    /**
     * 订单支付完成时间
     */
    @ApiModelProperty(value = "订单支付完成时间", required = true)
    private Date payFinishDateTime;
    /**
     * 提货时间
     */
    @ApiModelProperty(value = "提货时间", required = true)
    private Date pickupDateTime;
    /**
     * 订单完成时间
     */
    @ApiModelProperty(value = "订单完成时间", required = true)
    private Date finishDateTime;
    /**
     * 取消订单时间
     */
    @ApiModelProperty(value = "订单取消时间", required = true)
    private Date cancelDateTime;
    /**
     * 提货方式:1自提;2配送; 现阶段只有自提
     */
    @ApiModelProperty(value = "提货方式:1自提;2配送;", required = true)
    private Short pickupType;
    /**
     * 取消原因
     */
    @ApiModelProperty(value = "取消原因", required = true)
    private String cancelReason;
    /**
     * 订单备注
     */
    @ApiModelProperty(value = "订单备注", required = true)
    private String remark;
}
