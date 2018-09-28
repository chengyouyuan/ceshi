package com.winhxd.b2c.common.domain.customer.condition;

import com.winhxd.b2c.common.domain.common.ApiCondition;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * sunwenwu
 * 2018-9-27
 */
@ApiModel("请求参数")
@Data
public class CustomerListByPhoneCondition{
    @ApiModelProperty(value = "用户手机号集合")
    private List<String> customerPhones;
}
