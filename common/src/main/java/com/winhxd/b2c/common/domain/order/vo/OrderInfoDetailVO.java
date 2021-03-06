package com.winhxd.b2c.common.domain.order.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.winhxd.b2c.common.domain.order.enums.OrderStatusEnum;
import com.winhxd.b2c.common.domain.order.enums.PickUpTypeEnum;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author pangjianhua
 * @date 2018/8/3 9:52
 */
public class OrderInfoDetailVO implements Serializable {
    private static final long serialVersionUID = -2960449858467596227L;

    @ApiModelProperty(value = "订单商品详情")
    private List<OrderItemVO> orderItemVoList;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "订单编号")
    @Excel(name = "订单编码", width = 20)
    private String orderNo;

    @ApiModelProperty(value = "提货方式描述")
    @Excel(name = "取货方式", width = 10)
    private String pickupTypeDesc;

    @ApiModelProperty(value = "下单用户ID")
    private Long customerId;

    @ApiModelProperty(value = "用户昵称")
    @Excel(name = "下单用户", width = 20)
    private String nickName;

    @ApiModelProperty(value = "客户手机号码")
    @Excel(name = "用户电话", width = 20)
    private String customerMobile;

    @ApiModelProperty(value = "送货上门收货人姓名;")
    @Excel(name = "收货人", width = 30)
    private String orderConsignee = "-";

    @ApiModelProperty(value = "送货上门收货人电话;")
    @Excel(name = "收货电话", width = 20)
    private String orderConsigneeMobile = "-";

    @ApiModelProperty(value = "送货上门收货人电话;")
    @Excel(name = "收货地址", width = 50)
    private String orderAddress = "-";

    @ApiModelProperty(value = "商家名称")
    @Excel(name = "下单商铺", width = 50)
    private String storeName;

    @ApiModelProperty(value = "商家电话")
    @Excel(name = "商铺电话", width = 20)
    private String storeMobile;

    @ApiModelProperty(value = "接单门店ID")
    private Long storeId;

    @ApiModelProperty(value = "计价类型:1:线上计价;2:线下计价;")
    private Short valuationType;

    @ApiModelProperty(value = "计价类型描述")
    @Excel(name = "计价类型", width = 10)
    private String valuationTypeDesc;

    @ApiModelProperty(value = "订单创建时间")
    @Excel(name = "下单时间", width = 20, exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date created;

    @ApiModelProperty(value = "订单商品总数量")
    @Excel(name = "商品总数", width = 10)
    private Integer skuQuantity;

    @ApiModelProperty(value = "所用优惠券标题")
    @Excel(name = "所用优惠券", width = 20)
    private String couponTitles;

    @ApiModelProperty(value = "订单总金额")
    @Excel(name = "订单金额", width = 20)
    private BigDecimal orderTotalMoney;

    @ApiModelProperty(value = "优惠金额")
    @Excel(name = "优惠金额", width = 20)
    private BigDecimal discountMoney;

    @ApiModelProperty(value = "订单实付金额")
    @Excel(name = "实付金额", width = 20)
    private BigDecimal realPaymentMoney;

    @ApiModelProperty(value = "订单状态 1:已提交;2:待付款;3:待接单;5:待计价;7:已计价;9:待自提(已确认);10:待配送(已确认);11:待顾客确认;13:已完成;22:已完成;99:已取消;77:已退款;33:待退款;88:退款失败;", required = true)
    private Short orderStatus;

    @ApiModelProperty(value = "订单状态描述")
    @Excel(name = "订单状态", width = 10)
    private String orderStatusDesc;

    @ApiModelProperty(value = "提货码")
    private String pickupCode;

    @ApiModelProperty(value = "订单商品种类数量")
    private Integer skuCategoryQuantity;

    @ApiModelProperty(value = "支付类型:2为微信扫码付款;1为微信在线支付;")
    private Short payType;

    @ApiModelProperty(value = "支付类型描述")
    private String payTypeDesc;

    @ApiModelProperty(value = "订单支付流水号")
    private String paymentSerialNum;

    @ApiModelProperty(value = "支付状态:0为未支付;1为已支付;")
    private Short payStatus;

    @ApiModelProperty(value = "支付状态描述")
    private String payStatusDesc;

    @ApiModelProperty(value = "订单支付完成时间")
    private Date payFinishDateTime;

    @ApiModelProperty(value = "订单完成时间")
    private Date finishDateTime;

    @ApiModelProperty(value = "订单取消时间")
    private Date cancelDateTime;

    @ApiModelProperty(value = "接单时间")
    private Date acceptOrderDatetime;

    @ApiModelProperty(value = "退款完成时间")
    private Date refundDateTime;

    @ApiModelProperty(value = "申请退款时间")
    private Date applyRefundDatetime;

    @ApiModelProperty(value = "提货方式:1:到店自提;2:送货上门;")
    private Short pickupType;

    @ApiModelProperty(value = "提货时间")
    private Date pickupDateTime;

    @ApiModelProperty(value = "取消原因")
    private String cancelReason;

    @ApiModelProperty(value = "订单备注")
    private String remarks;

    @ApiModelProperty(value = "用户头像")
    private String headImg;

    @ApiModelProperty(value = "退款失败原因")
    @Excel(name = "退款失败原因", width = 50)
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
                orderStatusDesc = OrderStatusEnum.SUBMITTED.getStatusMark();
            }
            if(this.orderStatus.equals(OrderStatusEnum.WAIT_PAY.getStatusCode())){
                orderStatusDesc = OrderStatusEnum.WAIT_PAY.getStatusMark();
            }
            if(this.orderStatus.equals(OrderStatusEnum.UNRECEIVED.getStatusCode())){
                orderStatusDesc = OrderStatusEnum.UNRECEIVED.getStatusMark();
            }
            if(this.orderStatus.equals(OrderStatusEnum.ALREADY_VALUATION.getStatusCode())){
                orderStatusDesc = OrderStatusEnum.ALREADY_VALUATION.getStatusMark();
            }
            if(this.orderStatus.equals(OrderStatusEnum.WAIT_SELF_LIFTING.getStatusCode())){
                orderStatusDesc = OrderStatusEnum.WAIT_SELF_LIFTING.getStatusMark();
            }
            if(this.orderStatus.equals(OrderStatusEnum.WAIT_DELIVERY.getStatusCode())){
                orderStatusDesc = OrderStatusEnum.WAIT_DELIVERY.getStatusMark();
            }
            if(this.orderStatus.equals(OrderStatusEnum.FINISHED.getStatusCode())){
                orderStatusDesc = OrderStatusEnum.FINISHED.getStatusMark();
            }
            if(this.orderStatus.equals(OrderStatusEnum.WAIT_REFUND.getStatusCode())){
                orderStatusDesc = OrderStatusEnum.WAIT_REFUND.getStatusMark();
            }
            if(this.orderStatus.equals(OrderStatusEnum.REFUNDING.getStatusCode())){
                orderStatusDesc = OrderStatusEnum.REFUNDING.getStatusMark();
            }
            if(this.orderStatus.equals(OrderStatusEnum.REFUNDED.getStatusCode())){
                orderStatusDesc = OrderStatusEnum.REFUNDED.getStatusMark();
            }
            if(this.orderStatus.equals(OrderStatusEnum.REFUND_FAIL.getStatusCode())){
                orderStatusDesc = OrderStatusEnum.REFUND_FAIL.getStatusMark();
            }
            if(this.orderStatus.equals(OrderStatusEnum.CANCELED.getStatusCode())){
                orderStatusDesc = OrderStatusEnum.CANCELED.getStatusMark();
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