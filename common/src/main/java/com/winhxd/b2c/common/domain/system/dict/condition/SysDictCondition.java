package com.winhxd.b2c.common.domain.system.dict.condition;

import com.winhxd.b2c.common.domain.common.PagedCondition;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("字典组")
@Data
public class SysDictCondition extends PagedCondition {

    @ApiModelProperty("编码")
    private String code;

    @ApiModelProperty("名称")
    private String name;

}