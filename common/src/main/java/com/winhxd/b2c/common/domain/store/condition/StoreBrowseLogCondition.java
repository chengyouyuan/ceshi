package com.winhxd.b2c.common.domain.store.condition;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.winhxd.b2c.common.domain.common.ApiCondition;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liutong
 * @date 2018-08-11 18:48:35
 */
@ApiModel("惠小店基础信息保存入参")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class StoreBrowseLogCondition extends ApiCondition {

    @ApiModelProperty(value = "门店用户id", required = true)
    private Long storeCustomerId;

}
