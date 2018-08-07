package com.winhxd.b2c.common.domain.system.region.condition;

import com.winhxd.b2c.common.domain.base.condition.BaseCondition;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @description: ${description}
 * @author: zhanglingke
 * @create: 2018-08-06 11:39
 **/
public class SysRegionCondition {

    @ApiModelProperty(value = "区域行政级别")
    private Integer level;

    @ApiModelProperty(value = "区域编号", required = true)
    private String regionCode;

    public Integer getLevel() {
        return level;
    }
    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getRegionCode() {
        return regionCode;
    }
}
