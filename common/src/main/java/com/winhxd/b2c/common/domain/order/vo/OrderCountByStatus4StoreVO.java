package com.winhxd.b2c.common.domain.order.vo;

import io.swagger.annotations.ApiModelProperty;

public class OrderCountByStatus4StoreVO {

    @ApiModelProperty(value = "全部订单数量")
    private Integer allNum = 0;
    
    @ApiModelProperty(value = "待接单数量")
    private Integer unreceivedNum = 0;
    
    @ApiModelProperty(value = "待自提数量")
    private Integer waitSelfLiftingNum = 0;
    
    @ApiModelProperty(value = "待退款数量")
    private Integer waitRefundNum = 0;
    
    @ApiModelProperty(value = "待付款数量")
    private Integer waitPayNum = 0;
    
    @ApiModelProperty(value = "已完成数量")
    private Integer finishedNum = 0;
    
    @ApiModelProperty(value = "已退款数量")
    private Integer refundedNum = 0;
    
    @ApiModelProperty(value = "退款中数量")
    private Integer refundingNum = 0;
    
    @ApiModelProperty(value = "已取消数量")
    private Integer canceledNum = 0;

    public Integer getAllNum() {
        return allNum;
    }

    public void setAllNum(Integer allNum) {
        this.allNum = allNum;
    }

    public Integer getUnreceivedNum() {
        return unreceivedNum;
    }

    public void setUnreceivedNum(Integer unreceivedNum) {
        this.unreceivedNum = unreceivedNum;
    }

    public Integer getWaitSelfLiftingNum() {
        return waitSelfLiftingNum;
    }

    public void setWaitSelfLiftingNum(Integer waitSelfLiftingNum) {
        this.waitSelfLiftingNum = waitSelfLiftingNum;
    }

    public Integer getWaitRefundNum() {
        return waitRefundNum;
    }

    public void setWaitRefundNum(Integer waitRefundNum) {
        this.waitRefundNum = waitRefundNum;
    }

    public Integer getWaitPayNum() {
        return waitPayNum;
    }

    public void setWaitPayNum(Integer waitPayNum) {
        this.waitPayNum = waitPayNum;
    }

    public Integer getFinishedNum() {
        return finishedNum;
    }

    public void setFinishedNum(Integer finishedNum) {
        this.finishedNum = finishedNum;
    }

    public Integer getRefundedNum() {
        return refundedNum;
    }

    public void setRefundedNum(Integer refundedNum) {
        this.refundedNum = refundedNum;
    }

    public Integer getCanceledNum() {
        return canceledNum;
    }

    public void setCanceledNum(Integer canceledNum) {
        this.canceledNum = canceledNum;
    }

    public Integer getRefundingNum() {
        return refundingNum;
    }

    public void setRefundingNum(Integer refundingNum) {
        this.refundingNum = refundingNum;
    }

    @Override
    public String toString() {
        return "OrderCountByStatus4StoreVO [allNum=" + allNum + ", unreceivedNum=" + unreceivedNum
                + ", waitSelfLiftingNum=" + waitSelfLiftingNum + ", waitRefundNum=" + waitRefundNum + ", waitPayNum="
                + waitPayNum + ", finishedNum=" + finishedNum + ", refundedNum=" + refundedNum + ", refundingNum="
                + refundingNum + ", canceledNum=" + canceledNum + "]";
    }
    
    
}
