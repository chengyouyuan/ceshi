package com.winhxd.b2c.common.domain.store.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 门店用户绑定关系表
 */
@ApiModel("门店用户绑定关系表")
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class StoreCustomerRelation {

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("用户id主键")
    private Long customerId;

    @ApiModelProperty("商店id主键")
    private Long storeUserId;

    @ApiModelProperty("绑定时间")
    private Date bindingTime;

    @ApiModelProperty("绑定状态,1已经绑定，0未绑定")
    private Integer status;

    @ApiModelProperty("解绑时间")
    private Date unbindingTime;
}