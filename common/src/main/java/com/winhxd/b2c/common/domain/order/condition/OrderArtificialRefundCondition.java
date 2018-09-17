package com.winhxd.b2c.common.domain.order.condition;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author pangjianhua
 * @date 2018/9/12 10:05
 */
@Data
public class OrderArtificialRefundCondition implements Serializable {
    private static final long serialVersionUID = 2357391764186944611L;

    @Data
    public static class OrderList {
        private String orderNo;
    }

    @ApiModelProperty("门店列表")
    private List<OrderArtificialRefundCondition.OrderList> list = new ArrayList<>();
}
