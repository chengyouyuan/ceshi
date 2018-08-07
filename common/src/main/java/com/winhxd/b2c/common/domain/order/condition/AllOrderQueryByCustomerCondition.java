package com.winhxd.b2c.common.domain.order.condition;

import com.winhxd.b2c.common.domain.base.condition.BaseCondition;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author pangjianhua
 * @date 2018/8/6 19:12
 */
@Data
public class AllOrderQueryByCustomerCondition extends BaseCondition implements Serializable {

    private static final long serialVersionUID = -4668408534907732545L;

    @ApiModelProperty(value = "提货码")
    private String pickUpCode;
}