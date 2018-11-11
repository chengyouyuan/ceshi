package com.winhxd.b2c.common.domain.order.vo;

import com.winhxd.b2c.common.domain.order.enums.OrderStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author pangjianhua
 * @date 2018/8/13 16:28
 */
public class OrderListForCustomerVO {

    @ApiModelProperty(value = "订单商品详情", required = true)
    private List<OrderListItemForCustomerVO> orderItemVoList;
    @ApiModelProperty(value = "订单编号", required = true)
    private String orderNo;
    @ApiModelProperty(value = "订单状态 1:已提交;2:待付款；3:待接单;7:已计价; 9:待自提(已确认);10:待配送(已确认);11:待顾客确认;22:已完成;99:已取消;77:已退款;33:待退款；66:退款中；", required = true)
    private Short orderStatus;
    @ApiModelProperty(value = "订单状态描述", required = true)
    private String orderStatusDesc;
    @ApiModelProperty(value = "订单总金额", required = true)
    private BigDecimal orderTotalMoney;
    @ApiModelProperty(value = "订单实付金额", required = true)
    private BigDecimal realPaymentMoney;
    @ApiModelProperty(value = "订单创建时间", required = true)
    private Date created;

    public List<OrderListItemForCustomerVO> getOrderItemVoList() {
        return orderItemVoList;
    }

    public void setOrderItemVoList(List<OrderListItemForCustomerVO> orderItemVoList) {
        this.orderItemVoList = orderItemVoList;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
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

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
