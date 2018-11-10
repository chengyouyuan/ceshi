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
     * 订单状态 1:已提交;2:待付款；3:待接单;7:已计价;
     * 9:待自提(已确认);10:待送货(已确认);11:待顾客确认;22:已完成;99:已取消;77:已退款;33:待退款；66:退款中；88:退款失败；
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
     * 优惠券标题
     */
    private String couponTitles;
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
    private BigDecimal realPaymentMoney;

    /**
     * 订单商品总数量
     */
    private Integer skuQuantity;

    /**
     * 订单商品种类数量
     */
    private Integer skuCategoryQuantity;
    /**
     * 支付类型:2:微信扫码付款;1为微信在线支付;
     */
    private Short payType;
    /**
     * 支付状态:0为未支付;1为已支付;
     */
    private Short payStatus;

    /**
     * 支付成功流水号
     */
    private String paymentSerialNum;
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
     * 退款时间
     */
    private Date refundDateTime;
    /**
     * 申请退款时间
     */
    private Date applyRefundDatetime;
    /**
     * 接单时间
     */
    private Date acceptOrderDatetime;
    /**
     * 提货类型:1: 门店自提;2: 送货上门;
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
    private String remarks;
    /**
     * 下单所用设备
     */
    private String imie;
    /**
     * 下单门店区域编码
     */
    private String regionCode;
    /**
     * 退款失败原因
     */
    private String refundFailReason;
    private Date updated;
    private Long createdBy;
    private Long updatedBy;
    private String updatedByName;
    private String createdByName;

    /**
     * 订单收货人
     */
    private String orderConsignee;
    /**
     * 订单收货人电话
     */
    private String orderConsigneeMobile;
    /**
     * 订单收货地址
     */
    private String orderAddress;


    /**
     * 订单商品项
     */
    private List<OrderItem> orderItems;

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

    public Short getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Short orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPickupCode() {
        return pickupCode;
    }

    public void setPickupCode(String pickupCode) {
        this.pickupCode = pickupCode;
    }

    public BigDecimal getRandomReductionMoney() {
        return randomReductionMoney;
    }

    public void setRandomReductionMoney(BigDecimal randomReductionMoney) {
        this.randomReductionMoney = randomReductionMoney;
    }

    public String getCouponTitles() {
        return couponTitles;
    }

    public void setCouponTitles(String couponTitles) {
        this.couponTitles = couponTitles;
    }

    public BigDecimal getCouponHxdMoney() {
        return couponHxdMoney;
    }

    public void setCouponHxdMoney(BigDecimal couponHxdMoney) {
        this.couponHxdMoney = couponHxdMoney;
    }

    public BigDecimal getCouponBrandMoney() {
        return couponBrandMoney;
    }

    public void setCouponBrandMoney(BigDecimal couponBrandMoney) {
        this.couponBrandMoney = couponBrandMoney;
    }

    public BigDecimal getOrderTotalMoney() {
        return orderTotalMoney;
    }

    public void setOrderTotalMoney(BigDecimal orderTotalMoney) {
        this.orderTotalMoney = orderTotalMoney;
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

    public Short getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Short payStatus) {
        this.payStatus = payStatus;
    }

    public String getPaymentSerialNum() {
        return paymentSerialNum;
    }

    public void setPaymentSerialNum(String paymentSerialNum) {
        this.paymentSerialNum = paymentSerialNum;
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

    public Date getPickupDateTime() {
        return pickupDateTime;
    }

    public void setPickupDateTime(Date pickupDateTime) {
        this.pickupDateTime = pickupDateTime;
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

    public Date getAcceptOrderDatetime() {
        return acceptOrderDatetime;
    }

    public void setAcceptOrderDatetime(Date acceptOrderDatetime) {
        this.acceptOrderDatetime = acceptOrderDatetime;
    }

    public Short getPickupType() {
        return pickupType;
    }

    public void setPickupType(Short pickupType) {
        this.pickupType = pickupType;
    }

    public String getInviterCode() {
        return inviterCode;
    }

    public void setInviterCode(String inviterCode) {
        this.inviterCode = inviterCode;
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

    public String getImie() {
        return imie;
    }

    public void setImie(String imie) {
        this.imie = imie;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getRefundFailReason() {
        return refundFailReason;
    }

    public void setRefundFailReason(String refundFailReason) {
        this.refundFailReason = refundFailReason;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedByName() {
        return updatedByName;
    }

    public void setUpdatedByName(String updatedByName) {
        this.updatedByName = updatedByName;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
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

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}