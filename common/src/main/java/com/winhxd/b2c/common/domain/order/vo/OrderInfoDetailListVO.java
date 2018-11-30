package com.winhxd.b2c.common.domain.order.vo;


import cn.afterturn.easypoi.excel.annotation.Excel;
import com.winhxd.b2c.common.domain.order.enums.OrderStatusEnum;
import com.winhxd.b2c.common.domain.order.enums.PickUpTypeEnum;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: zhoufenglong
 * @Description: 订单商品详情表
 * @param:
 * @return：
 * @Date: 2018/11/29 17:09
 */
public class OrderInfoDetailListVO implements Serializable {
    private static final long serialVersionUID = -2960449858467596227L;


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

    @ApiModelProperty(value = "商品SKU")
    @Excel(name = "商品SKU", width = 20)
    private String skuCode;

    @ApiModelProperty(value = "商品名称")
    @Excel(name = "商品名称", width = 50)
    private String skuDesc;

    @ApiModelProperty(value = "商品价格，未计价商品有可能为空")
    @Excel(name = "商品单价", width = 10)
    private BigDecimal price;

    @ApiModelProperty(value = "商品数量")
    @Excel(name = "商品数量", width = 20)
    private Integer amount;

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

    @ApiModelProperty(value = "退款失败原因")
    @Excel(name = "退款失败原因", width = 50)
    private String refundFailReason;

    @ApiModelProperty(value = "提货方式:1:到店自提;2:送货上门;")
    private Short pickupType;

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

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
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

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getSkuDesc() {
        return skuDesc;
    }

    public void setSkuDesc(String skuDesc) {
        this.skuDesc = skuDesc;
    }

    public String getRefundFailReason() {
        return refundFailReason;
    }

    public void setRefundFailReason(String refundFailReason) {
        this.refundFailReason = refundFailReason;
    }
}