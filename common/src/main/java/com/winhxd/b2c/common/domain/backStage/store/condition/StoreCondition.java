package com.winhxd.b2c.common.domain.backStage.store.condition;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by caiyulong on 2018/8/4.
 */
@ApiModel("后台门店请求参数")
@Data
public class StoreCondition {

    @ApiModelProperty(value = "编号")
    private Long userId;
}
