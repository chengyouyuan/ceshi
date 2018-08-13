package com.winhxd.b2c.common.domain.store.condition;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.winhxd.b2c.common.domain.common.ApiCondition;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 惠小店基础信息保存入参
 *
 * @author liutong
 * @date 2018-08-03 14:22:44
 */
@ApiModel("惠小店基础信息保存入参")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class StoreBaseInfoCondition extends ApiCondition {

    @ApiModelProperty(value = "门店名称", required = true)
    private String storeName;

    @ApiModelProperty(value = "店主姓名", required = true)
    private String shopkeeper;

    @ApiModelProperty(value = "店主头像", required = true)
    private String shopOwnerImg;

    @ApiModelProperty(value = "区域编码", required = true)
    private String storeRegionCode;

    @ApiModelProperty(value = "门店地址", required = true)
    private String storeAddress;
}
