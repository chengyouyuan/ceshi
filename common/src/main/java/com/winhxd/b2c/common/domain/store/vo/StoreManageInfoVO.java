package com.winhxd.b2c.common.domain.store.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 惠小店管理首页数据查询返参
 * @author liutong
 * @date 2018-08-04 09:37:57
 */
@ApiModel("惠小店管理首页数据查询返参")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class StoreManageInfoVO {

    @ApiModelProperty(value = "小店id（首页分享时用）")
    private Long businessId;

    @ApiModelProperty(value = "门店名称（首页展示用）")
    private String storeName;

    @ApiModelProperty(value = "门店别名（首页展示用取姓名第一个字加上老板）")
    private String storeBoss;

    @ApiModelProperty(value = "今日营业额", required = true)
    private BigDecimal turnover;

    @ApiModelProperty(value = "今日浏览人数", required = true)
    private Integer browseNum;

    @ApiModelProperty(value = "今日下单人数", required = true)
    private Integer createNum;

    @ApiModelProperty(value = "今日订单数", required = true)
    private Integer completeNum;

    @ApiModelProperty(value = "对比昨日营业额")
    private String turnoverCompare;

    @ApiModelProperty(value = "对比昨日浏览人数")
    private String browseNumCompare;

    @ApiModelProperty(value = "对比昨日下单人数")
    private String createNumCompare;

    @ApiModelProperty(value = "对比昨日订单数")
    private String completeNumCompare;

    @ApiModelProperty(value = "未读消息数(今日未读+历史未读)")
    private Integer neteaseMessageNum;

}
