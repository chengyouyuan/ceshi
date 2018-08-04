package com.winhxd.b2c.common.domain.store.condition;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 惠小店开店验证入参
 *
 * @author liutong
 * @date 2018-08-03 10:37:12
 */
@ApiModel("惠小店开店验证入参")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class OpenStoreCondition {
    @ApiModelProperty(value = "门店编码", required = true)
    private Long storeId;

    @ApiModelProperty(value = "用户编码", required = true)
    private Long customerId;


}
