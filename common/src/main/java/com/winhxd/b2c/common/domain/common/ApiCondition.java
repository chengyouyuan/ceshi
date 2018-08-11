package com.winhxd.b2c.common.domain.common;

import io.swagger.annotations.ApiModelProperty;

public abstract class ApiCondition {
    @ApiModelProperty(value = "版本号")
    private String ver;

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }
}
