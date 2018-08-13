package com.winhxd.b2c.common.domain.system.region.condition;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description: 地理区域上送参数
 * @author: zhanglingke
 * @create: 2018-08-06 14:11
 **/
@Data
public class SysRegionCodeCondition {

    @ApiModelProperty(value = "区域编号", required = true)
    private String regionCode;

    public String getRegionCode() {
        return regionCode;
    }

    public SysRegionCodeCondition(String regionCode) {
        this.regionCode = regionCode;
    }
}
