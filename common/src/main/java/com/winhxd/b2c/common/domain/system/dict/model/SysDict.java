package com.winhxd.b2c.common.domain.system.dict.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@ApiModel("字典组")
@Data
public class SysDict {

    @ApiModelProperty(value = "编号")
    private Long id;

    @ApiModelProperty("编码")
    private String code;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("创建时间")
    private Date created;

    @ApiModelProperty("创建人id")
    private Long createdBy;

    @ApiModelProperty("创建人")
    private String createdByName;

    @ApiModelProperty("更新人id")
    private Long updatedBy;

    @ApiModelProperty("更新时间")
    private Date updated;

    @ApiModelProperty("更新人")
    private String updatedByName;

    @ApiModelProperty("字典项")
    private List<SysDictItem> items;

}