package com.winhxd.b2c.common.domain.system.dict.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("字典项")
@Data
public class SysDictItem {

    @ApiModelProperty(value = "编号")
    private Long id;

    @ApiModelProperty("字典组编号")
    private Long dictId;

    @ApiModelProperty("值")
    private String value;

    @ApiModelProperty("名称")
    private String name;

}