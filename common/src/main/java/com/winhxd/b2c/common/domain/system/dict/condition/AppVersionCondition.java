package com.winhxd.b2c.common.domain.system.dict.condition;

import com.winhxd.b2c.common.domain.common.ApiCondition;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("app版本号")
@Data
public class AppVersionCondition extends ApiCondition {

    @ApiModelProperty("app版本号")
    private String appVersion;

}
