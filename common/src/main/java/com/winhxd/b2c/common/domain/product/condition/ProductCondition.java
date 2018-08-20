package com.winhxd.b2c.common.domain.product.condition;

import java.util.List;

import com.winhxd.b2c.common.domain.product.enums.SearchSkuCodeEnum;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 查询商品信息 condition
 * @ClassName:  ProductCondition   
 * @Description:TODO   
 * @author: zhuchongchen 
 * @date:   2018年8月4日 下午1:06:11   
 *
 */

@Data
public class ProductCondition {
	
	@ApiModelProperty(value = "在sku集合范围内/外查找", required = true)
	private SearchSkuCodeEnum searchSkuCode;
	
    @ApiModelProperty(value = "门店在惠下单购买过商品sku")
    private List<String> hxdProductSkus;
    
    @ApiModelProperty(value = "门店已上架商品sku", required = true)
    private List<String> productSkus;
    
    @ApiModelProperty(value = "店主推荐sku集合")
    private List<String> recommendSkus;
    
    @ApiModelProperty(value = "品牌编码")
    private List<String> brandCodes;
    
    @ApiModelProperty(value = "一级品类编码")
    private String categoryCode;
    
    @ApiModelProperty(value = "二级品类编码")
    private List<String> categoryCodes;
    
    @ApiModelProperty(value = "商品名称")
    private String productName;
    
    @ApiModelProperty(value = "商品编码")
    private String productCode;
    
    @ApiModelProperty(value = "商品编码")
    private List<String> productCodes;
}
