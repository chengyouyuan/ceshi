package com.winhxd.b2c.common.domain.store.condition;

import com.winhxd.b2c.common.domain.common.PagedCondition;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * @Description: 后台门店查询条件
 * @author chengyy
 * @date 2018/8/13 19:15
 */
@Data
@ApiModel(value = "门店请求参数",description = "后台门店列表请求参数")
public class BackStageStoreInfoSimpleCondition extends PagedCondition {

    @ApiModelProperty("门店名称")
    private String storeName;

    @ApiModelProperty("联系方式")
    private String contactMobile;

    @ApiModelProperty("id主键")
    private Long id;
}
