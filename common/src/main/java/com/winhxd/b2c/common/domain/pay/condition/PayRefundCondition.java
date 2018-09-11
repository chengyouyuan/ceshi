package com.winhxd.b2c.common.domain.pay.condition;

import com.winhxd.b2c.common.domain.common.ApiCondition;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author lizhonghua
 * @date  2018年8月14日11:09:35
 * @Description 微信退款
 * @version 
 */
public class PayRefundCondition extends ApiCondition implements Serializable {

	private static final long serialVersionUID = 5825339934872843457L;

	@ApiModelProperty("商户订单号")
	private String outTradeNo;

	@ApiModelProperty("订单金额：元")
	private BigDecimal totalAmount;

	@ApiModelProperty("退款金额：元")
	private BigDecimal refundAmount;

	@ApiModelProperty("退款原因")
	private String refundDesc;

	@ApiModelProperty("创建人ID")
	private Long createdBy;

	@ApiModelProperty("创建人姓名")
	private String createdByName;

	@ApiModelProperty("订单号")
	private String orderNo;

	@ApiModelProperty("支付成功流水号")
	private String paymentSerialNum;

	private Long updatedBy;

	private String updatedByName;

	@ApiModelProperty("取消原因")
	private String cancelReason;

	public String getPaymentSerialNum() {
		return paymentSerialNum;
	}

	public void setPaymentSerialNum(String paymentSerialNum) {
		this.paymentSerialNum = paymentSerialNum;
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

	public String getCancelReason() {
		return cancelReason;
	}

	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public BigDecimal getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(BigDecimal refundAmount) {
		this.refundAmount = refundAmount;
	}

	public String getRefundDesc() {
		return refundDesc;
	}

	public void setRefundDesc(String refundDesc) {
		this.refundDesc = refundDesc;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedByName() {
		return createdByName;
	}

	public void setCreatedByName(String createdByName) {
		this.createdByName = createdByName;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
}
