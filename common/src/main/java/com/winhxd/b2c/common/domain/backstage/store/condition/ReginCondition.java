package com.winhxd.b2c.common.domain.backstage.store.condition;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by caiyulong on 2018/8/6.
 */
@ApiModel("后台门店管理获取地域请求参数")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ReginCondition {

    @ApiModelProperty(value = "查询地域级别 1省 2市 3县 4镇、乡 5村")
    private Integer level;

    @ApiModelProperty(value = "父reginCode 用于查询子地域集合")
    private Integer parentReginCode;
}
