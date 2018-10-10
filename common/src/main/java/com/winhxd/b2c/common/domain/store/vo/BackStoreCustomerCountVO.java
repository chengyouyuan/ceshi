package com.winhxd.b2c.common.domain.store.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by sunwenwu on 2018/9/28.
 */
@ApiModel("后台-门店和用户的统计返参")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class BackStoreCustomerCountVO {

    @ApiModelProperty(value = "门店信息-区域统计")
    private List<String> stores;

    @ApiModelProperty(value = "门店下总用户数")
    private Long storeCustomerNum;

    @ApiModelProperty(value = "门店信息-导入门店")
    List<StoreUserInfoVO> storesInfos;

}
