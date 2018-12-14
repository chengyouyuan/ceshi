package com.winhxd.b2c.common.domain.store.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("门店测试区域配置")
public class StoreRegionVO {
    @ApiModelProperty(value = "id主键")
    private Long id;

    @ApiModelProperty(value = "配送范围区域name")
    @Excel(name = "测试区域名称", width = 30)
    private String areaName;

    @ApiModelProperty(value = "区域code")
    private String areaCode;

    @ApiModelProperty(value = "地理区域级别")
    @Excel(name = "地理区域级别", width = 30)
    private Short level;

    @ApiModelProperty(value = "更新人")
    private String updatedBy;

    @ApiModelProperty(value = "更新时间")
    private Date updated;

    @ApiModelProperty(value = "创建时间")
    @Excel(name = "创建时间", width = 30, exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date created;

    @ApiModelProperty(value = "创建人")
    @Excel(name = "创建人", width = 30)
    private String createdBy;

}