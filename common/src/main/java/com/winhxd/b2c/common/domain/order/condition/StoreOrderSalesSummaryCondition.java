package com.winhxd.b2c.common.domain.order.condition;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 门店每日订单销售数据汇总 查询条件
 * @author wangbin
 * @date  2018年8月4日 上午10:07:46
 * @version 
 */
@Data
public class StoreOrderSalesSummaryCondition {
    
    /**
     * 按天查询 汇总
     */
    public static final short INTRADAY_ORDER_SALES_QUERY_TYPE = 1;
    /**
     * 按月查询 汇总
     */
    public static final short MONTH_ORDER_SALES_QUERY_TYPE = 2;

    @ApiModelProperty(value = "门店Id")
    private Long storeId;
    
    @ApiModelProperty(value = "查询范围：1 当天订单销售数据汇总，2=月售", required=true)
    private Short queryPeriodType;
}
