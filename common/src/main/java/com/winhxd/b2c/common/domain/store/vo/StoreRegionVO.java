package com.winhxd.b2c.common.domain.store.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("门店测试区域配置")
public class StoreRegionVO {
    @ApiModelProperty(value = "id主键")
    private Long id;

    @ApiModelProperty(value = "配送范围区域name")
    private String areaName;

    @ApiModelProperty(value = "区域code")
    private String areaCode;

    @ApiModelProperty(value = "地理区域级别")
    private Short level;

    @ApiModelProperty(value = "更新人")
    private String updatedBy;

    @ApiModelProperty(value = "更新时间")
    private String createdBy;

    @ApiModelProperty(value = "创建人")
    private Date created;

    @ApiModelProperty(value = "创建时间")
    private Date updated;

}