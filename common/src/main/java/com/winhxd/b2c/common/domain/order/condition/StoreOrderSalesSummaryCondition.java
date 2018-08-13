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
    
    @ApiModelProperty(value = "查询开始区间")
    private Date startDateTime;
    
    @ApiModelProperty(value = "查询开始区间")
    private Date endDateTime;

    @ApiModelProperty(value = "门店Id")
    public Long storeId;
}
