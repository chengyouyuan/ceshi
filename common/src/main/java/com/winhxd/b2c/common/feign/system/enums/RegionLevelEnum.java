package com.winhxd.b2c.common.feign.system.enums;

/**
 * @description: 行政区域级别
 * @author: zhanglingke
 * @create: 2018-08-07 10:45
 **/
public enum RegionLevelEnum {

    PROVINCELEVEL(1, "省"),
    CITYLEVEL(2, "市"),
    COUNTYLEVEL(3, "区县"),
    TOWNLEVEL(4, "乡镇"),
    VILLAGELEVEL(5, "居委会，村");

    private Integer code;
    private String desc;

    RegionLevelEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
