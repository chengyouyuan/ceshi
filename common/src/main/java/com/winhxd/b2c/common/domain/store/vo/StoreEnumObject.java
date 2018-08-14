package com.winhxd.b2c.common.domain.store.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liutong
 * @date 2018-08-13 16:51:20
 */
@Data
@ApiModel("枚举列表内部对象")
public class StoreEnumObject {

    @ApiModelProperty(value = "编码", required = true)
    private Short code;

    @ApiModelProperty(value = "名称", required = true)
    private String name;

    @ApiModelProperty(value = "选中状态（0否，1是）", required = true)
    private Short status;

}
