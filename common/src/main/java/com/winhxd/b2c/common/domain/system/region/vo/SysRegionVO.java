package com.winhxd.b2c.common.domain.system.region.vo;

import com.winhxd.b2c.common.domain.system.region.model.SysRegion;
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
public class SysRegionVO extends SysRegion {

    @ApiModelProperty(value = "区域名称")
    private String name;

    @ApiModelProperty(value = "区域完整名称")
    private String fullName;

}