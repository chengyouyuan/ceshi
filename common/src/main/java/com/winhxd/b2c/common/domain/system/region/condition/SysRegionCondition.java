package com.winhxd.b2c.common.domain.system.region.condition;

import com.winhxd.b2c.common.domain.common.ApiCondition;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description: ${description}
 * @author: zhanglingke
 * @create: 2018-08-06 11:39
 **/
@Data
public class SysRegionCondition {

    @ApiModelProperty(value = "区域编号", required = true)
    private String regionCode;

    @ApiModelProperty(value = "区域行政级别（1=省，2=市，3=区县，4=乡/镇，5=村/居委会）")
    private Integer level;
}
