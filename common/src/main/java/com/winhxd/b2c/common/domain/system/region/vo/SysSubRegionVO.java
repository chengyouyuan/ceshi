package com.winhxd.b2c.common.domain.system.region.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description: 地理区域VO
 * @auther: zhanglingke
 * @date: 2018-VO-02 15:25
 */
@ApiModel("子集地理区域")
@Data
public class SysSubRegionVO {

    @ApiModelProperty(value = "区域编号")
    private String regionCode;

    @ApiModelProperty(value = "区域名称")
    private String name;

    @ApiModelProperty(value = "区域行政级别")
    private Integer level;

    @ApiModelProperty(value = "父级区域编号")
    private Integer parentRegionCode;
}