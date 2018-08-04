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

    @ApiModelProperty(value = "门店有效状态 1有效 2无效")
    private Integer storeStatus;

    @ApiModelProperty(value = "门店账号")
    private String storeMobile;

    @ApiModelProperty(value = "门店名称")
    private String storeName;

    @ApiModelProperty(value = "省code")
    private String provinceCode;

    @ApiModelProperty(value = "市code")
    private String cityCode;

    @ApiModelProperty(value = "县code")
    private String countyCode;

    @ApiModelProperty(value = "乡/镇code")
    private String townCode;

    @ApiModelProperty(value = "村code")
    private String villageCode;
}
