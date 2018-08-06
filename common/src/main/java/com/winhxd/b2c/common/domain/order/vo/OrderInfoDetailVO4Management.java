package com.winhxd.b2c.common.domain.order.vo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

public class OrderInfoDetailVO4Management {

    @ApiModelProperty(value = "订单详情", required = true)
    private OrderInfoDetailVO orderInfoDetailVO;
    
    @ApiModelProperty(value = "订单状态变化列表", required = true)
    private List<OrderChangeVO> orderChangeVoList = new ArrayList<>();

    public OrderInfoDetailVO getOrderInfoDetailVO() {
        return orderInfoDetailVO;
    }

    public void setOrderInfoDetailVO(OrderInfoDetailVO orderInfoDetailVO) {
        this.orderInfoDetailVO = orderInfoDetailVO;
    }

    public List<OrderChangeVO> getOrderChangeVoList() {
        return orderChangeVoList;
    }

    public void setOrderChangeVoList(List<OrderChangeVO> orderChangeVoList) {
        this.orderChangeVoList = orderChangeVoList;
    }

    @Override
    public String toString() {
        return "OrderInfoDetailVO4Management [orderInfoDetailVO=" + orderInfoDetailVO + ", orderChangeVoList="
                + Arrays.toString(orderChangeVoList.toArray(new OrderChangeVO[orderChangeVoList.size()])) + "]";
    }
}
