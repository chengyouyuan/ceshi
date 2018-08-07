package com.winhxd.b2c.common.domain.order.vo;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

public class OrderChangeVO {

    @ApiModelProperty(value = "订单状态修改描述")
    private String changeMsg;
    
    @ApiModelProperty(value = "订单状态修改时间")
    private Date changeDateTime;

    public String getChangeMsg() {
        return changeMsg;
    }

    public void setChangeMsg(String changeMsg) {
        this.changeMsg = changeMsg;
    }

    public Date getChangeDateTime() {
        return changeDateTime;
    }

    public void setChangeDateTime(Date changeDateTime) {
        this.changeDateTime = changeDateTime;
    }

    @Override
    public String toString() {
        return "OrderChangeVO [changeMsg=" + changeMsg + ", changeDateTime=" + changeDateTime + "]";
    }
}
