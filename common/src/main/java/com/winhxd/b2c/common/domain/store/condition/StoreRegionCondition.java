package com.winhxd.b2c.common.domain.store.condition;

import com.winhxd.b2c.common.domain.common.PagedCondition;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author: wangbaokuo
 * @date: 2018/8/10 15:12
 */
@Data
@ApiModel("门店测试区域配置查询条件")
public class StoreRegionCondition extends PagedCondition {

    @ApiModelProperty(value = "区域code")
    private String areaCode;

    @ApiModelProperty(value = "配送范围区域")
    private String areaName;

    @ApiModelProperty(value = "地理区域级别")
    private Short level;

    @ApiModelProperty(value = "创建时间")
    private Date created;

    @ApiModelProperty("是否查询全部数据")
    private Boolean isQueryAll = false;
}
