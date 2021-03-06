package com.winhxd.b2c.common.domain.store.condition;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.winhxd.b2c.common.domain.common.PagedCondition;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 后台门店账户管理请求参数
 *
 * @author caiyulong
 * @date 2018/8/4
 */
@ApiModel("后台门店账户管理请求参数")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class BackStageStoreInfoCondition extends PagedCondition {

    @ApiModelProperty(value = "门店有效状态 1有效 2无效")
    private Short storeStatus;

    @ApiModelProperty(value = "门店用户编码")
    private Long storeCustomerId;

    @ApiModelProperty(value = "门店账号")
    private String storeMobile;

    @ApiModelProperty(value = "门店名称")
    private String storeName;

    @ApiModelProperty(value = "筛选的最小级别regionCode")
    private String regionCode;

    @ApiModelProperty(value = "筛选的最小级别regionCode集合")
    private List<String> regionCodeList;

    @ApiModelProperty("是否查询全部数据")
    private Boolean isQueryAll = false;

}
