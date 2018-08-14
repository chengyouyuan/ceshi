package com.winhxd.b2c.common.domain.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 通用Api请求参数
 */
@Data
public class AdminPagedCondition {
    @ApiModelProperty("页号")
    protected int pageNo = 1;

    @ApiModelProperty("每页条数")
    protected int pageSize = 50;

    @ApiModelProperty("排序字段和排序方式(例：name asc,age desc)")
    private String orderBy;
}
