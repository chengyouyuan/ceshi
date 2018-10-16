package com.winhxd.b2c.common.domain.system.dict.condition;

import com.winhxd.b2c.common.domain.common.ApiCondition;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("字典项")
@Data
public class SysDictItemCondition extends ApiCondition {

    @ApiModelProperty("值")
    private String appVersion;

}
