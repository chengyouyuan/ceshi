package com.winhxd.b2c.common.domain.system.region.condition;

import io.swagger.annotations.ApiModelProperty;

/**
 * @description: 地理区域上送参数
 * @author: zhanglingke
 * @create: 2018-08-06 14:11
 **/
public class SysRegionCodeCondition {

    @ApiModelProperty(value = "区域编号", required = true)
    private String regionCode;

    public String getRegionCode() {
        return regionCode;
    }
}
