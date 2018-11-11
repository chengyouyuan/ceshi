package com.winhxd.b2c.common.domain.order.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.winhxd.b2c.common.domain.order.enums.OrderStatusEnum;
import com.winhxd.b2c.common.domain.order.enums.PickUpTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author pangjianhua
 * @date 2018/8/3 9:52
 */
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
    @ApiModelProperty(value = "计价类型描述", required = true)
    private String valuationTypeDesc;
    /**
     * 订单状态 1:已提交;2:待付款;3:待接单;5:待计价;7:已计价;
     * 9:待自提(已确认);10:待配送(已确认);11:待顾客确认;13:已完成;99:已取消;77:已退款;33:待退款;
     */
    @ApiModelProperty(value = "订单状态 2:待付款;3:待接单;9:待自提(已确认);10:待配送(已确认);11:待顾客确认;22:已完成;99:已取消;77:已退款;33:待退款;88:退款失败;", required = true)
    private Short orderStatus;
    @ApiModelProperty(value = "订单状态描述", required = true)
    private String orderStatusDesc;
    /**
     * 提货码
     */
    @ApiModelProperty(value = "提货码", required = true)
    private String pickupCode;

    /**
     * 提货码
     */
    @ApiModelProperty(value = "所以用优惠券标题", required = true)
    private String couponTitles;

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

    @ApiModelProperty(value = "订单商品总数量", required = true)
    private Integer skuQuantity;

    @ApiModelProperty(value = "订单商品种类数量", required = true)
    private Integer skuCategoryQuantity;

    /**
     * 支付类型:1为微信扫码付款;2为微信在线支付;
     */
    @ApiModelProperty(value = "支付类型:2为微信扫码付款;1为微信在线支付;", required = true)
    private Short payType;
    @ApiModelProperty(value = "支付类型描述", required = true)
    private String payTypeDesc;

    @ApiModelProperty(value = "订单支付流水号", required = false)
    private String paymentSerialNum;
    /**
     * 支付状态:0为未支付;1为已支付;
     */
    @ApiModelProperty(value = "支付状态:0为未支付;1为已支付;", required = true)
    private Short payStatus;
    @ApiModelProperty(value = "支付状态描述", required = true)
    private String payStatusDesc;
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
     * 订单完成时间
     */
    @ApiModelProperty(value = "订单完成时间", required = true)
    private Date finishDateTime;
    /**
     * 取消订单时间
     */
    @ApiModelProperty(value = "订单取消时间", required = true)
    private Date cancelDateTime;

    @ApiModelProperty(value = "接单时间", required = true)
    private Date acceptOrderDatetime;
    @ApiModelProperty(value = "退款完成时间", required = true)
    private Date refundDateTime;
    @ApiModelProperty(value = "申请退款时间", required = true)
    private Date applyRefundDatetime;
    @ApiModelProperty(value = "提货方式:1:到店自提;2:送货上门;", required = true)
    private Short pickupType;
    @ApiModelProperty(value = "提货方式描述", required = true)
    private String pickupTypeDesc;
    @ApiModelProperty(value = "提货时间", required = true)
    private Date pickupDateTime;
    @ApiModelProperty(value = "送货上门收货人姓名;", required = false)
    private String orderConsignee;
    @ApiModelProperty(value = "送货上门收货人电话;", required = false)
    private String orderConsigneeMobile;
    @ApiModelProperty(value = "送货上门收货人电话;", required = false)
    private String orderAddress;
    @ApiModelProperty(value = "取消原因", required = true)
    private String cancelReason;
    @ApiModelProperty(value = "订单备注", required = true)
    private String remarks;
    @ApiModelProperty(value = "客户手机号码", required = true)
    private String customerMobile;
    @ApiModelProperty(value = "用户昵称", required = true)
    private String nickName;
    @ApiModelProperty(value = "商家电话", required = true)
    private String storeMobile;
    @ApiModelProperty(value = "商家名称", required = true)
    private String storeName;
    @ApiModelProperty(value = "用户头像")
    private String headImg;
    @ApiModelProperty(value = "退款失败原因")
    private String refundFailReason;

    public List<OrderItemVO> getOrderItemVoList() {
        return orderItemVoList;
    }

    public void setOrderItemVoList(List<OrderItemVO> orderItemVoList) {
        this.orderItemVoList = orderItemVoList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Short getValuationType() {
        return valuationType;
    }

    public void setValuationType(Short valuationType) {
        this.valuationType = valuationType;
    }

    public String getValuationTypeDesc() {
        return valuationTypeDesc;
    }

    public void setValuationTypeDesc(String valuationTypeDesc) {
        this.valuationTypeDesc = valuationTypeDesc;
    }

    public Short getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Short orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatusDesc() {
        if (null != this.orderStatus) {
            if(this.orderStatus.equals(OrderStatusEnum.SUBMITTED.getStatusCode())){
                orderStatusDesc = OrderStatusEnum.SUBMITTED.getStatusDes();
            }
            if(this.orderStatus.equals(OrderStatusEnum.WAIT_PAY.getStatusCode())){
                orderStatusDesc = OrderStatusEnum.WAIT_PAY.getStatusDes();
            }
            if(this.orderStatus.equals(OrderStatusEnum.UNRECEIVED.getStatusCode())){
                orderStatusDesc = OrderStatusEnum.UNRECEIVED.getStatusDes();
            }
            if(this.orderStatus.equals(OrderStatusEnum.ALREADY_VALUATION.getStatusCode())){
                orderStatusDesc = OrderStatusEnum.ALREADY_VALUATION.getStatusDes();
            }
            if(this.orderStatus.equals(OrderStatusEnum.WAIT_SELF_LIFTING.getStatusCode())){
                orderStatusDesc = OrderStatusEnum.WAIT_SELF_LIFTING.getStatusDes();
            }
            if(this.orderStatus.equals(OrderStatusEnum.WAIT_DELIVERY.getStatusCode())){
                orderStatusDesc = OrderStatusEnum.WAIT_DELIVERY.getStatusDes();
            }
            if(this.orderStatus.equals(OrderStatusEnum.FINISHED.getStatusCode())){
                orderStatusDesc = OrderStatusEnum.FINISHED.getStatusDes();
            }
            if(this.orderStatus.equals(OrderStatusEnum.WAIT_REFUND.getStatusCode())){
                orderStatusDesc = OrderStatusEnum.WAIT_REFUND.getStatusDes();
            }
            if(this.orderStatus.equals(OrderStatusEnum.REFUNDING.getStatusCode())){
                orderStatusDesc = OrderStatusEnum.REFUNDING.getStatusDes();
            }
            if(this.orderStatus.equals(OrderStatusEnum.REFUNDED.getStatusCode())){
                orderStatusDesc = OrderStatusEnum.REFUNDED.getStatusDes();
            }
            if(this.orderStatus.equals(OrderStatusEnum.REFUND_FAIL.getStatusCode())){
                orderStatusDesc = OrderStatusEnum.REFUND_FAIL.getStatusDes();
            }
            if(this.orderStatus.equals(OrderStatusEnum.CANCELED.getStatusCode())){
                orderStatusDesc = OrderStatusEnum.CANCELED.getStatusDes();
            }
        }
        return orderStatusDesc;
    }

    public void setOrderStatusDesc(String orderStatusDesc) {
        this.orderStatusDesc = orderStatusDesc;
    }

    public String getPickupCode() {
        return pickupCode;
    }

    public void setPickupCode(String pickupCode) {
        this.pickupCode = pickupCode;
    }

    public String getCouponTitles() {
        return couponTitles;
    }

    public void setCouponTitles(String couponTitles) {
        this.couponTitles = couponTitles;
    }

    public BigDecimal getOrderTotalMoney() {
        return orderTotalMoney;
    }

    public void setOrderTotalMoney(BigDecimal orderTotalMoney) {
        this.orderTotalMoney = orderTotalMoney;
    }

    public BigDecimal getDiscountMoney() {
        return discountMoney;
    }

    public void setDiscountMoney(BigDecimal discountMoney) {
        this.discountMoney = discountMoney;
    }

    public BigDecimal getRealPaymentMoney() {
        return realPaymentMoney;
    }

    public void setRealPaymentMoney(BigDecimal realPaymentMoney) {
        this.realPaymentMoney = realPaymentMoney;
    }

    public Integer getSkuQuantity() {
        return skuQuantity;
    }

    public void setSkuQuantity(Integer skuQuantity) {
        this.skuQuantity = skuQuantity;
    }

    public Integer getSkuCategoryQuantity() {
        return skuCategoryQuantity;
    }

    public void setSkuCategoryQuantity(Integer skuCategoryQuantity) {
        this.skuCategoryQuantity = skuCategoryQuantity;
    }

    public Short getPayType() {
        return payType;
    }

    public void setPayType(Short payType) {
        this.payType = payType;
    }

    public String getPayTypeDesc() {
        return payTypeDesc;
    }

    public void setPayTypeDesc(String payTypeDesc) {
        this.payTypeDesc = payTypeDesc;
    }

    public String getPaymentSerialNum() {
        return paymentSerialNum;
    }

    public void setPaymentSerialNum(String paymentSerialNum) {
        this.paymentSerialNum = paymentSerialNum;
    }

    public Short getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Short payStatus) {
        this.payStatus = payStatus;
    }

    public String getPayStatusDesc() {
        return payStatusDesc;
    }

    public void setPayStatusDesc(String payStatusDesc) {
        this.payStatusDesc = payStatusDesc;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getPayFinishDateTime() {
        return payFinishDateTime;
    }

    public void setPayFinishDateTime(Date payFinishDateTime) {
        this.payFinishDateTime = payFinishDateTime;
    }

    public Date getFinishDateTime() {
        return finishDateTime;
    }

    public void setFinishDateTime(Date finishDateTime) {
        this.finishDateTime = finishDateTime;
    }

    public Date getCancelDateTime() {
        return cancelDateTime;
    }

    public void setCancelDateTime(Date cancelDateTime) {
        this.cancelDateTime = cancelDateTime;
    }

    public Date getAcceptOrderDatetime() {
        return acceptOrderDatetime;
    }

    public void setAcceptOrderDatetime(Date acceptOrderDatetime) {
        this.acceptOrderDatetime = acceptOrderDatetime;
    }

    public Date getRefundDateTime() {
        return refundDateTime;
    }

    public void setRefundDateTime(Date refundDateTime) {
        this.refundDateTime = refundDateTime;
    }

    public Date getApplyRefundDatetime() {
        return applyRefundDatetime;
    }

    public void setApplyRefundDatetime(Date applyRefundDatetime) {
        this.applyRefundDatetime = applyRefundDatetime;
    }

    public Short getPickupType() {
        return pickupType;
    }

    public void setPickupType(Short pickupType) {
        this.pickupType = pickupType;
    }

    public String getPickupTypeDesc() {
        if (null != this.pickupType) {
            if (this.pickupType.equals(PickUpTypeEnum.SELF_PICK_UP)) {
                pickupTypeDesc = PickUpTypeEnum.SELF_PICK_UP.getTypeDesc();
            }
            if (this.pickupType.equals(PickUpTypeEnum.DELIVERY_PICK_UP)) {
                pickupTypeDesc = PickUpTypeEnum.DELIVERY_PICK_UP.getTypeDesc();
            }
        }
        return pickupTypeDesc;
    }

    public void setPickupTypeDesc(String pickupTypeDesc) {
        this.pickupTypeDesc = pickupTypeDesc;
    }

    public Date getPickupDateTime() {
        return pickupDateTime;
    }

    public void setPickupDateTime(Date pickupDateTime) {
        this.pickupDateTime = pickupDateTime;
    }

    public String getOrderConsignee() {
        return orderConsignee;
    }

    public void setOrderConsignee(String orderConsignee) {
        this.orderConsignee = orderConsignee;
    }

    public String getOrderConsigneeMobile() {
        return orderConsigneeMobile;
    }

    public void setOrderConsigneeMobile(String orderConsigneeMobile) {
        this.orderConsigneeMobile = orderConsigneeMobile;
    }

    public String getOrderAddress() {
        return orderAddress;
    }

    public void setOrderAddress(String orderAddress) {
        this.orderAddress = orderAddress;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getStoreMobile() {
        return storeMobile;
    }

    public void setStoreMobile(String storeMobile) {
        this.storeMobile = storeMobile;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getRefundFailReason() {
        return refundFailReason;
    }

    public void setRefundFailReason(String refundFailReason) {
        this.refundFailReason = refundFailReason;
    }
}