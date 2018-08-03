package com.winhxd.b2c.common.domain.order.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 订单主表
 *
 * @author pangjianhua
 * @date 2018/8/2 14:50
 */
@Data
public class OrderInfo {

    /**
     * 主键
     */
    private Long id;
    /**
     * 订单编号
     */
    private String orderNo;
    /**
     * 下单用户ID
     */
    private Long customerId;
    /**
     * 接单门店ID
     */
    private Long storeId;
    /**
     * 计价类型:1:线上计价;2:线下计价;
     */
    private Short valuationType;
    /**
     * 订单状态 1:已提交;3:待接单;5:待计价;7:已计价;
     * 9:待自提(已确认);11:待顾客确认;13:已完成;99:已取消;77:已退款;33:待退款;
     */
    private Short orderStatus;
    /**
     * 提货码
     */
    private String pickupCode;
    /**
     * 随机立减金额
     */
    private BigDecimal randomReductionMoney;
    /**
     * 惠下单优惠金额
     */
    private BigDecimal couponHxdMoney;
    /**
     * 品牌商优惠金额
     */
    private BigDecimal couponBrandMoney;
    /**
     * 订单总金额
     */
    private BigDecimal orderTotalMoney;
    /**
     * 实付金额
     */
    private Long realPaymentMoney;
    /**
     * 支付类型:1为微信扫码付款;2为微信在线支付;
     */
    private Short payType;
    /**
     * 支付状态:0为未支付;1为已支付;
     */
    private Short payStatus;
    /**
     * 订单创建时间
     */
    private Date created;
    /**
     * 订单支付完成时间
     */
    private Date payFinishDateTime;
    /**
     * 提货时间
     */
    private Date pickupDateTime;
    /**
     * 订单完成时间
     */
    private Date finishDateTime;
    /**
     * 取消订单时间
     */
    private Date cancelDateTime;
    /**
     * 提货方式:1自提;2配送; 现阶段只有自提
     */
    private Short pickupType;
    /**
     * 邀请码
     */
    private String inviterCode;
    /**
     * 取消原因
     */
    private String cancelReason;
    /**
     * 订单备注
     */
    private String remark;
    /**
     * 下单所用设备
     */
    private String imie;
    /**
     * 下单门店区域编码
     */
    private String regionCode;
    private Date updated;
    private Long createdBy;
    private Long updatedBy;
    private String updatedByName;
    private String createdByName;
    
    /**
     * 订单商品项
     */
    private List<OrderItem> orderItems;
}