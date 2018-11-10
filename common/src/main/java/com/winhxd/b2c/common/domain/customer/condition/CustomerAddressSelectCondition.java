package com.winhxd.b2c.common.domain.customer.condition;

import com.winhxd.b2c.common.domain.common.ApiCondition;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: sunwenwu
 * @Date: 2018/11/8 14：26
 * @Description:
 */
@ApiModel("C端用户查询收货地址-请求参数")
@Data
public class CustomerAddressSelectCondition extends ApiCondition {

    @ApiModelProperty(value = "收货地址主键ID",required = true)
    private Long id;
}
