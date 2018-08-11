package com.winhxd.b2c.common.domain.store.condition;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.winhxd.b2c.common.domain.condition.MobileCondition;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @title: retail2c-backend
 * @description: B端门店上架商品搜索condtion
 * @author: lvsen
 * @date: 2018/8/11 11:15
 * @version: 1.0
 */
@ApiModel("B端门店上架商品搜索")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class StorePutawayProdSearchCondition extends MobileCondition {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品名称")
    private String prodName;

    @ApiModelProperty(value = "商品类型默认0，0 惠下单商品，1 普通商品")
    private Byte prodType;

    @ApiModelProperty(value = "是否是首次请求默认true，true是 false否")
    private Boolean first = true;

    @ApiModelProperty(value = "页大小")
    private Integer pageSize;

    @ApiModelProperty(value = "页号")
    private Integer pageNo;

}