package com.winhxd.b2c.common.domain.store.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 惠小店管理首页数据查询返参
 * @author liutong
 * @date 2018-08-04 09:37:57
 */
@ApiModel("惠小店管理首页数据查询返参")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class StoreManageInfoVO {

    @ApiModelProperty(value = "门店名称", required = true)
    private String storeName;

    @ApiModelProperty(value = "今日营业额", required = true)
    private String turnover;

    @ApiModelProperty(value = "今日浏览人数", required = true)
    private String browseNum;

    @ApiModelProperty(value = "今日下单人数", required = true)
    private String createNum;

    @ApiModelProperty(value = "今日订单数", required = true)
    private String completeNum;

    @ApiModelProperty(value = "对比昨日营业额")
    private String turnoverCompare;

    @ApiModelProperty(value = "对比昨日浏览人数")
    private String browseNumCompare;

    @ApiModelProperty(value = "对比昨日下单人数")
    private String createNumCompare;

    @ApiModelProperty(value = "对比昨日订单数")
    private String completeNumCompare;

}
