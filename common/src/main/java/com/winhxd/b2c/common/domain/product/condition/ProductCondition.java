package com.winhxd.b2c.common.domain.product.condition;

import java.util.List;

import com.winhxd.b2c.common.domain.product.enums.CallClientEnums;

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
	
	@ApiModelProperty(value = "调用方 B端 或 C端")
	private CallClientEnums client;
	
    @ApiModelProperty(value = "门店在惠下单购买过商品sku")
    private List<String> hxdProductSkus;
    
    @ApiModelProperty(value = "门店已上架商品sku")
    private List<String> productSkus;
    
    @ApiModelProperty(value = "品牌编码")
    private List<String> brandCodes;
    
    @ApiModelProperty(value = "一级品类编码")
    private String categoryCode;
    
    @ApiModelProperty(value = "二级品类编码")
    private List<String> categoryCodes;
    
    @ApiModelProperty(value = "商品名称")
    private String productName;

	@ApiModelProperty(value = "商品排序 1最新到货 2价格排序 3销量排序")
	private Integer prodSort;

	@ApiModelProperty(value = "门店编号")
	private Integer storeId;

	@ApiModelProperty(value = "每页显示条数")
    private Integer pageSize;
    
	@ApiModelProperty(value = "页号")
    private Integer pageNo;
}