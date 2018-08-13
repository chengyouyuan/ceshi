<<<<<<< HEAD
package com.winhxd.b2c.common.domain.order.condition;

import com.winhxd.b2c.common.domain.base.condition.BaseCondition;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author pangjianhua
 * @date 2018/8/3 17:12
 */
@Data
public class OrderQueryByCustomerCondition {
    @ApiModelProperty(value = "订单编号，查询订单详情时需传递编号")
    private String orderNo;
}
=======
package com.winhxd.b2c.common.domain.order.condition;

import com.winhxd.b2c.common.domain.common.ApiCondition;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author pangjianhua
 * @date 2018/8/3 17:12
 */
@Data
public class OrderQueryByCustomerCondition extends ApiCondition {
    @ApiModelProperty(value = "订单编号，查询订单详情时需传递编号")
    private String orderNo;
}
>>>>>>> branch 'master' of git@192.168.1.101:retail2c/retail2c-backend.git
