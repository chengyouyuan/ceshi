package com.winhxd.b2c.common.domain.store.condition;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.winhxd.b2c.common.domain.common.ApiCondition;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hupengtao
 * @date 2018-09-18 19:48:35
 */
@ApiModel("检验绑定门店信息入参")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class StoreCheckBindingCondition extends ApiCondition {

    @ApiModelProperty(value = "请求绑定的门店Id")
    private Long storeId;
}
