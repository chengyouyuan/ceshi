package com.winhxd.b2c.common.domain.system.region.model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel
public class SysRegion implements Serializable{

    private static final long serialVersionUID = -2960449858467596217L;
    @ApiModelProperty(value = "地理区域编号")
    private String regionCode;

    @ApiModelProperty(value = "省")
    private String province;

    @ApiModelProperty(value = "市")
    private String city;

    @ApiModelProperty(value = "区县")
    private String town;

    @ApiModelProperty(value = "乡镇")
    private String county;

    @ApiModelProperty(value = "居委会，村")
    private String village;

    @ApiModelProperty(value = "省编号")
    private String provinceCode;

    @ApiModelProperty(value = "市编号")
    private String cityCode;

    @ApiModelProperty(value = "区县编号")
    private String countyCode;

    @ApiModelProperty(value = "乡镇编号")
    private String townCode;

    @ApiModelProperty(value = "居委会，村编号")
    private String villageCode;

     /**
     * 地域级别
     * 1.	省
     * 2.	市
     * 3.	区县
     * 4.	/镇（街道）
     * 5.	村（居委会）
     */
     @ApiModelProperty(value = "区域行政级别（1=省，2=市，3=区县，4=乡/镇，5=村/居委会）")
    private Integer level;

    @ApiModelProperty(value = "111=居委会")
    private Integer villageType;
}