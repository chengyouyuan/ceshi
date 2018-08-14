package com.winhxd.b2c.common.domain.system.region.condition;

import com.winhxd.b2c.common.domain.common.AdminPagedCondition;
import com.winhxd.b2c.common.domain.common.ApiCondition;
import com.winhxd.b2c.common.domain.common.PagedCondition;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description: ${description}
 * @author: zhanglingke
 * @create: 2018-08-06 11:39
 **/
@Data
public class SysRegionPagedCondition extends AdminPagedCondition {

    @ApiModelProperty(value = "区域编号", required = true)
    private String regionCode;

    @ApiModelProperty(value = "区域行政级别（1=省，2=市，3=区县，4=乡/镇，5=村/居委会）")
    private Integer level;

    @ApiModelProperty(value = "省")
    private String province;

    @ApiModelProperty(value = "市")
    private String city;

    @ApiModelProperty(value = "区县")
    private String county;

    @ApiModelProperty(value = "乡镇")
    private String town;

    @ApiModelProperty(value = "居委会，村")
    private String village;
}
