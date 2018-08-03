package com.winhxd.b2c.common.domain.order.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author pangjianhua
 * @date 2018/8/3 10:07
 */
@Data
public class OrderInfoListVO {
    @ApiModelProperty(value = "订单列表", required = true)
    private List<OrderInfoDetailVO> orderInfoDetailVOList;
}
