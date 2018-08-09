package com.winhxd.b2c.common.domain.backstage.store.condition;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 后台门店账户管理保存门店入参
 *
 * @author liutong
 * @date 2018-08-09 11:22:49
 */
@ApiModel("后台门店账户管理保存门店入参")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class BackStageModifyStoreCondition {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "门店有效状态 1有效 2无效")
    private Byte storeStatus;

    @ApiModelProperty(value = "门店名称")
    private String storeName;

    @ApiModelProperty(value = "门店地址")
    private String storeAddress;

    @ApiModelProperty(value = "联系人")
    private String shopkeeper;

    @ApiModelProperty(value = "联系方式")
    private String contactMobile;



}
