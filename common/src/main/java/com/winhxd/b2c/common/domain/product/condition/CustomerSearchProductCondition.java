package com.winhxd.b2c.common.domain.product.condition;

import com.winhxd.b2c.common.domain.base.condition.BaseCondition;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class CustomerSearchProductCondition extends BaseCondition {

    @ApiModelProperty(value = "门店编号", required = true)
    private Long storeId;

    @ApiModelProperty(value = "品牌编码")
    private List<String> brandCodes;

    @ApiModelProperty(value = "点击店主推荐分类传参 1推荐 ")
    private Short recommend;

    @ApiModelProperty(value = "一级品类编码")
    private String categoryCode;

    @ApiModelProperty(value = "二级品类编码")
    private List<String> categoryCodes;

    @ApiModelProperty(value = "排序 0最近到货 1价格排序 2销量排序")
    private Integer productSortType;

    @ApiModelProperty(value = "商品名称")
    private String productName;



}
