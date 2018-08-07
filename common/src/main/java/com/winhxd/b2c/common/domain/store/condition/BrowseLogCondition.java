package com.winhxd.b2c.common.domain.store.condition;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * C端用户浏览门店记录日志入参
 *
 * @author liutong
 * @date 2018-08-03 10:37:12
 */
@ApiModel("C端用户浏览门店记录日志入参")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class BrowseLogCondition {
    @ApiModelProperty(value = "门店编码", required = true)
    private Long storeId;

    @ApiModelProperty(value = "C端用户编码", required = true)
    private Long customerId;


}
