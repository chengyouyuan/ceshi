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
@ApiModel("惠小店浏览门店进入退出时记录日志保存入参")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class StoreBrowseLogCondition extends ApiCondition {

    @ApiModelProperty(value = "门店用户主键id", required = true)
    private Long storeId;

}
