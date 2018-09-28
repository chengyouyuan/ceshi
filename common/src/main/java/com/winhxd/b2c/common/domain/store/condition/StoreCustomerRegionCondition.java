package com.winhxd.b2c.common.domain.store.condition;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("根据店铺ID，店铺状态查询绑定的用户ID")
public class StoreCustomerRegionCondition{

    @ApiModelProperty(value = "店铺用户ID集合")
    private List<String> storeUserInfoIds;

    @ApiModelProperty(value = "惠小店状态")
    private String storeStatus;

}
